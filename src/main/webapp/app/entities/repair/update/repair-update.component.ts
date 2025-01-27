import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { Status } from 'app/entities/enumerations/status.model';
import { RepairService } from '../service/repair.service';
import { IRepair } from '../repair.model';
import { RepairFormService, RepairFormGroup } from './repair-form.service';
import { Type } from 'app/entities/enumerations/type.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';

@Component({
  standalone: true,
  selector: 'jhi-repair-update',
  templateUrl: './repair-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgSelectModule],
})
export class RepairUpdateComponent implements OnInit {
  @Input() isModal = false;
  @Input() set selectDevice(previousDevice: IDevice) {
    if (previousDevice.id != -1) {
      this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
        this.devicesSharedCollection,
        previousDevice,
      );

      this.editForm.get('device')?.setValue(previousDevice);
    }
  }
  @Output() closeModal = new EventEmitter<boolean>();
  @Output() completed = new EventEmitter<null>();
  isSaving = false;
  repair: IRepair | null = null;
  statusValues = Object.keys(Status);

  deviceSearchTerm = new Subject<string>();
  userSearchTerm = new Subject<string>();

  devicesSharedCollection: IDevice[] = [];
  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected repairService = inject(RepairService);
  protected repairFormService = inject(RepairFormService);
  protected deviceService = inject(DeviceService);
  protected activatedRoute = inject(ActivatedRoute);
  protected userService = inject(UserService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RepairFormGroup = this.repairFormService.createRepairFormGroup();

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ repair }) => {
      this.repair = repair;
      if (repair) {
        this.updateForm(repair);
      }

      this.loadRelationshipsOptions();
    });

    this.deviceSearchTerm.subscribe(term => {
      this.searchDevice(term);
    });

    this.userSearchTerm.subscribe(term => {
      this.searchUser(term);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('repairsApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const repair = this.repairFormService.getRepair(this.editForm);
    if (repair.id !== null) {
      this.subscribeToSaveResponse(this.repairService.update(repair));
    } else {
      this.subscribeToSaveResponse(this.repairService.create(repair));
    }
  }

  emitCloseModal(): void {
    this.closeModal.emit(true);
  }

  protected searchDevice(term: string): void {
    let deviceByType: IDevice[] = [];
    let deviceByBrand: IDevice[] = [];
    let deviceByModel: IDevice[] = [];
    let deviceBySerialNumber: IDevice[] = [];
    this.devicesSharedCollection = [];

    if (term && term.trim().length > 0) {
      this.deviceService.query({ 'brand.contains': term }).subscribe({
        next: response => {
          deviceByBrand = response.body ?? [];
        },
        complete: () => {
          this.deviceService.query({ 'model.contains': term }).subscribe({
            next: response => {
              deviceByModel = response.body ?? [];
            },
            complete: () => {
              this.deviceService.query({ 'serialNumber.contains': term }).subscribe({
                next: response => {
                  deviceBySerialNumber = response.body ?? [];
                },
                complete: () => {
                  for (const device of deviceByBrand) {
                    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
                      this.devicesSharedCollection,
                      device,
                    );
                  }
                  for (const device of deviceByModel) {
                    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
                      this.devicesSharedCollection,
                      device,
                    );
                  }
                  for (const device of deviceBySerialNumber) {
                    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
                      this.devicesSharedCollection,
                      device,
                    );
                  }
                  if (term.toUpperCase() in Type) {
                    this.deviceService.query({ 'type.in': [term.toUpperCase()] }).subscribe({
                      next: response => {
                        deviceByType = response.body ?? [];
                      },
                      complete: () => {
                        for (const device of deviceByType) {
                          this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
                            this.devicesSharedCollection,
                            device,
                          );
                        }
                      },
                    });
                  }
                },
              });
            },
          });
        },
      });
    } else {
      this.loadRelationshipsOptions();
    }
  }

  protected searchUser(term: string): void {
    this.usersSharedCollection = [];
    if (term && term.trim().length > 0) {
      this.userService.query({ 'login.contains': term }).subscribe({
        next: response => {
          this.usersSharedCollection = response.body ?? [];
        },
      });
    } else {
      this.loadRelationshipsOptions();
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRepair>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    if (this.isModal) {
      this.completed.emit(null);
    } else {
      this.previousState();
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(repair: IRepair): void {
    this.repair = repair;
    this.repairFormService.resetForm(this.editForm, repair);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(this.devicesSharedCollection, repair.device);
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, this.repair?.device)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.repair?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
