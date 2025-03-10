import { Component, NgZone, inject, input } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IDevice } from '../device.model';
import { FilterOption } from 'app/shared/filter';

@Component({
  standalone: true,
  selector: 'jhi-device-detail',
  templateUrl: './device-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DeviceDetailComponent {
  device = input<IDevice | null>(null);

  protected dataUtils = inject(DataUtils);
  public router = inject(Router);
  protected ngZone = inject(NgZone);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  protected seeOther(deviceId: number): void {
    const queryParamsObj: any = {
      page: 1,
      size: 20,
      sort: 'id,asc',
    };
    let filterOption: FilterOption;

    filterOption = new FilterOption('deviceId.equals', [deviceId.toString()]);
    queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;

    this.ngZone.run(() => {
      this.router.navigate(['/repair'], {
        queryParams: queryParamsObj,
      });
    });
  }
}
