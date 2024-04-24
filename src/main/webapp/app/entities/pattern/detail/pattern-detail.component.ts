import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPattern } from '../pattern.model';

@Component({
  standalone: true,
  selector: 'jhi-pattern-detail',
  templateUrl: './pattern-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PatternDetailComponent {
  pattern = input<IPattern | null>(null);

  previousState(): void {
    window.history.back();
  }
}
