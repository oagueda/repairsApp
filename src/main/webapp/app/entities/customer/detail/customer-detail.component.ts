import { Component, NgZone, OnInit, inject, input } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICustomer } from '../customer.model';
import { FilterOption, IFilterOption } from 'app/shared/filter/filter.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IDevice } from 'app/entities/device/device.model';

@Component({
  standalone: true,
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CustomerDetailComponent implements OnInit {
  customer = input<ICustomer | null>(null);
  devices: IDevice[] = [];
  devicesIds: string[] = [];

  protected ngZone = inject(NgZone);
  protected deviceService = inject(DeviceService);
  public router = inject(Router);

  ngOnInit(): void {
    this.deviceService.query({ 'customerId.equals': this.customer()?.id }).subscribe(res => {
      this.devices = res.body ?? [];
      for (const device of this.devices) {
        this.devicesIds.push(device.id.toString());
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  protected handleNavigation(route: string): void {
    const queryParamsObj: any = {
      page: 1,
      size: 20,
      sort: 'id,asc',
    };
    let filterOption: FilterOption;
    if (route === '/device') {
      filterOption = new FilterOption('customerId.equals', [this.customer()!.id.toString()]);
    } else {
      filterOption = new FilterOption('deviceId.in', this.devicesIds);
    }

    queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;

    this.ngZone.run(() => {
      this.router.navigate([route], {
        queryParams: queryParamsObj,
      });
    });
  }
}
