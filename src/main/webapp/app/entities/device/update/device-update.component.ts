import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPattern } from 'app/entities/pattern/pattern.model';
import { PatternService } from 'app/entities/pattern/service/pattern.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { Type } from 'app/entities/enumerations/type.model';
import { DeviceService } from '../service/device.service';
import { IDevice } from '../device.model';
import { DeviceFormService, DeviceFormGroup } from './device-form.service';

@Component({
  standalone: true,
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DeviceUpdateComponent implements OnInit {
  @Input() isModal = false;
  @Input() set selectCustomer(previousCustomer: ICustomer) {
    if (previousCustomer.id != -1) {
      this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
        this.customersSharedCollection,
        previousCustomer,
      );
      this.editForm.get('customer')?.setValue(previousCustomer);
    }
  }
  @Output() closeModal = new EventEmitter<boolean>();
  @Output() completed = new EventEmitter<IDevice>();
  isSaving = false;
  device: IDevice | null = null;
  typeValues = Object.keys(Type);

  patternsCollection: IPattern[] = [];
  customersSharedCollection: ICustomer[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected deviceService = inject(DeviceService);
  protected deviceFormService = inject(DeviceFormService);
  protected patternService = inject(PatternService);
  protected customerService = inject(CustomerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DeviceFormGroup = this.deviceFormService.createDeviceFormGroup();

  comparePattern = (o1: IPattern | null, o2: IPattern | null): boolean => this.patternService.comparePattern(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.device = device;
      if (device) {
        this.updateForm(device);
      }

      this.loadRelationshipsOptions();
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
    const device = this.deviceFormService.getDevice(this.editForm);
    if (device.id !== null) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  emitCloseModal(): void {
    this.closeModal.emit(true);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: response => this.onSaveSuccess(response.body ?? { id: -1 }),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(newDevice: IDevice): void {
    if (this.isModal) {
      this.completed.emit(newDevice);
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

  protected updateForm(device: IDevice): void {
    this.device = device;
    this.deviceFormService.resetForm(this.editForm, device);

    this.patternsCollection = this.patternService.addPatternToCollectionIfMissing<IPattern>(this.patternsCollection, device.pattern);
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      device.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patternService
      .query({ 'deviceId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPattern[]>) => res.body ?? []))
      .pipe(map((patterns: IPattern[]) => this.patternService.addPatternToCollectionIfMissing<IPattern>(patterns, this.device?.pattern)))
      .subscribe((patterns: IPattern[]) => (this.patternsCollection = patterns));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.device?.customer)),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
