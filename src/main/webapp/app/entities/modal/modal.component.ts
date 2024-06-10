import { Component, Input, OnInit, inject } from '@angular/core';
import { CustomerUpdateComponent } from '../customer/update/customer-update.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RepairUpdateComponent } from '../repair/update/repair-update.component';
import { DeviceUpdateComponent } from '../device/update/device-update.component';
import { Options } from '../enumerations/options.model';
import SharedModule from 'app/shared/shared.module';
import { MatStepper } from '@angular/material/stepper';
import { IDevice } from '../device/device.model';
import { ICustomer } from '../customer/customer.model';
import { PatternUpdateComponent } from '../pattern/update/pattern-update.component';
import { IPattern } from '../pattern/pattern.model';

@Component({
  standalone: true,
  templateUrl: 'modal.component.html',
  styleUrl: 'modal.component.scss',
  imports: [CustomerUpdateComponent, RepairUpdateComponent, DeviceUpdateComponent, SharedModule, PatternUpdateComponent],
})
export class ModalComponent implements OnInit {
  @Input() options!: Options[];

  selectedIndex = 0;
  optionsEnum = Options;

  previousCustomer?: ICustomer;
  previousDevice?: IDevice;

  activeModal = inject(NgbActiveModal);

  ngOnInit(): void {
    if (this.options.length < 1) {
      this.activeModal.dismiss();
    }
  }
  close(): void {
    this.activeModal.close();
  }

  setPattern(pattern: IPattern): void {
    this.activeModal.close(pattern);
  }

  next(event: ICustomer | IDevice | null, stepper: MatStepper): void {
    if (this.options.length == this.selectedIndex + 1) {
      this.activeModal.close();
      return;
    }
    if (event && event.id != -1) {
      if (this.selectedIndex == 0) {
        this.previousCustomer = event;
      } else {
        this.previousDevice = event;
      }
    }
    if (stepper.selected?.completed != null) {
      stepper.selected.completed = true;
      stepper.next();
    }
  }
}
