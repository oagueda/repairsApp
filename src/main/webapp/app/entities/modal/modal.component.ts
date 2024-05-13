import { Component, Input, OnInit, inject } from '@angular/core';
import { CustomerUpdateComponent } from '../customer/update/customer-update.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RepairUpdateComponent } from '../repair/update/repair-update.component';
import { DeviceUpdateComponent } from '../device/update/device-update.component';
import { Options } from '../enumerations/options.model';
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  templateUrl: 'modal.component.html',
  styleUrl: 'modal.component.scss',
  imports: [CustomerUpdateComponent, RepairUpdateComponent, DeviceUpdateComponent, SharedModule],
})
export class ModalComponent implements OnInit {
  @Input() options!: Options[];

  optionsEnum = Options;

  activeModal = inject(NgbActiveModal);

  ngOnInit(): void {
    if (this.options.length < 1) {
      this.activeModal.dismiss();
    }
  }
}
