import { Component, Input, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IRepair } from 'app/entities/repair/repair.model';
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  selector: 'jhi-board-task',
  templateUrl: './board-task.component.html',
  styleUrl: './board-task.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class BoardTaskComponent implements OnInit {
  @Input()
  repair!: IRepair;
  device: IDevice | null = null;
  customer: ICustomer | null = null;

  protected deviceService = inject(DeviceService);
  protected customerService = inject(CustomerService);

  ngOnInit(): void {
    this.getFullData();
  }

  protected getFullData(): void {
    if (this.repair.device?.id) {
      this.deviceService.find(this.repair.device.id).subscribe({
        next: response => {
          this.device = response.body;
          if (this.device?.customer?.id) {
            this.customerService.find(this.device.customer.id).subscribe({
              next: response => {
                this.customer = response.body;
              },
            });
          }
        },
      });
    }
  }
}
