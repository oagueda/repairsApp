import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPattern } from '../pattern.model';
import { PatternService } from '../service/pattern.service';
import { PatternFormService, PatternFormGroup } from './pattern-form.service';
import PatternLock from 'vanilla-pattern-lock';
import { IDevice } from 'app/entities/device/device.model';
@Component({
  standalone: true,
  selector: 'jhi-pattern-update',
  templateUrl: './pattern-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PatternUpdateComponent implements OnInit {
  @Input() isModal = false;
  isSaving = false;
  pattern: IPattern | null = null;

  lock = new PatternLock();

  @Output() closeModal = new EventEmitter<boolean>();
  @Output() completed = new EventEmitter<IDevice>();

  protected patternService = inject(PatternService);
  protected patternFormService = inject(PatternFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PatternFormGroup = this.patternFormService.createPatternFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pattern }) => {
      this.pattern = pattern;
      if (pattern) {
        this.updateForm(pattern);
      }
    });

    this.lock.render(document.getElementById('lockContainer')!).on('complete', (pattern: number) => {
      this.editForm.get('code')?.setValue(pattern.toString());
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pattern = this.patternFormService.getPattern(this.editForm);
    if (pattern.id !== null) {
      this.subscribeToSaveResponse(this.patternService.update(pattern));
    } else {
      this.subscribeToSaveResponse(this.patternService.create(pattern));
    }
  }

  emitCloseModal(): void {
    this.closeModal.emit(true);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPattern>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: response => this.onSaveSuccess(response.body ?? { id: -1 }),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(newPattern: IPattern): void {
    if (this.isModal) {
      this.completed.emit(newPattern);
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

  protected updateForm(pattern: IPattern): void {
    this.pattern = pattern;
    this.patternFormService.resetForm(this.editForm, pattern);
  }
}
