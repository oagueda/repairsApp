import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPattern, NewPattern } from '../pattern.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPattern for edit and NewPatternFormGroupInput for create.
 */
type PatternFormGroupInput = IPattern | PartialWithRequiredKeyOf<NewPattern>;

type PatternFormDefaults = Pick<NewPattern, 'id'>;

type PatternFormGroupContent = {
  id: FormControl<IPattern['id'] | NewPattern['id']>;
  code: FormControl<IPattern['code']>;
};

export type PatternFormGroup = FormGroup<PatternFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PatternFormService {
  createPatternFormGroup(pattern: PatternFormGroupInput = { id: null }): PatternFormGroup {
    const patternRawValue = {
      ...this.getFormDefaults(),
      ...pattern,
    };
    return new FormGroup<PatternFormGroupContent>({
      id: new FormControl(
        { value: patternRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(patternRawValue.code),
    });
  }

  getPattern(form: PatternFormGroup): IPattern | NewPattern {
    return form.getRawValue() as IPattern | NewPattern;
  }

  resetForm(form: PatternFormGroup, pattern: PatternFormGroupInput): void {
    const patternRawValue = { ...this.getFormDefaults(), ...pattern };
    form.reset(
      {
        ...patternRawValue,
        id: { value: patternRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PatternFormDefaults {
    return {
      id: null,
    };
  }
}
