import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IRepair } from '../repair.model';
import { RepairService } from '../service/repair.service';
import FileSaver from 'file-saver';
@Component({
  standalone: true,
  selector: 'jhi-repair-detail',
  templateUrl: './repair-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RepairDetailComponent {
  repair = input<IRepair | null>(null);

  protected dataUtils = inject(DataUtils);
  protected repairService = inject(RepairService);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
  print(): void {
    this.repairService.print(this.repair()!.id).subscribe({
      next: res => {
        if (!res.body) return;
        const file = new Blob([res.body], { type: 'application/pdf' });
        FileSaver.saveAs(
          file,
          this.repair()!.device?.customer?.name?.replace(/ /g, '') + '-' + this.repair()!.device?.customer?.nif?.replace(/ /g, ''),
        );
      },
    });
  }
}
