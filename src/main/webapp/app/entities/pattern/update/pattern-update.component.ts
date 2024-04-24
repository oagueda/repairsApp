import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPattern } from '../pattern.model';
import { PatternService } from '../service/pattern.service';
import { PatternFormService, PatternFormGroup } from './pattern-form.service';

@Component({
  standalone: true,
  selector: 'jhi-pattern-update',
  templateUrl: './pattern-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PatternUpdateComponent implements OnInit {
  isSaving = false;
  pattern: IPattern | null = null;

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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPattern>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
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
