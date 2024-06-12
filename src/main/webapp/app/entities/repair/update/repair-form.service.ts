import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRepair, NewRepair } from '../repair.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRepair for edit and NewRepairFormGroupInput for create.
 */
type RepairFormGroupInput = IRepair | PartialWithRequiredKeyOf<NewRepair>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRepair | NewRepair> = Omit<T, 'closedDate'> & {
  closedDate?: string | null;
};

type RepairFormRawValue = FormValueOf<IRepair>;

type NewRepairFormRawValue = FormValueOf<NewRepair>;

type RepairFormDefaults = Pick<NewRepair, 'id' | 'closedDate' | 'importantData'>;

type RepairFormGroupContent = {
  id: FormControl<RepairFormRawValue['id'] | NewRepair['id']>;
  description: FormControl<RepairFormRawValue['description']>;
  observations: FormControl<RepairFormRawValue['observations']>;
  internalObservations: FormControl<RepairFormRawValue['internalObservations']>;
  status: FormControl<RepairFormRawValue['status']>;
  closedDate: FormControl<RepairFormRawValue['closedDate']>;
  budget: FormControl<RepairFormRawValue['budget']>;
  workDone: FormControl<RepairFormRawValue['workDone']>;
  usedMaterial: FormControl<RepairFormRawValue['usedMaterial']>;
  customerMaterial: FormControl<RepairFormRawValue['customerMaterial']>;
  importantData: FormControl<RepairFormRawValue['importantData']>;
  total: FormControl<RepairFormRawValue['total']>;
  device: FormControl<RepairFormRawValue['device']>;
  user: FormControl<RepairFormRawValue['user']>;
};

export type RepairFormGroup = FormGroup<RepairFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RepairFormService {
  createRepairFormGroup(repair: RepairFormGroupInput = { id: null }): RepairFormGroup {
    const repairRawValue = this.convertRepairToRepairRawValue({
      ...this.getFormDefaults(),
      ...repair,
    });
    return new FormGroup<RepairFormGroupContent>({
      id: new FormControl(
        { value: repairRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(repairRawValue.description, {
        validators: [Validators.required],
      }),
      observations: new FormControl(repairRawValue.observations),
      internalObservations: new FormControl(repairRawValue.internalObservations),
      status: new FormControl(repairRawValue.status, {
        validators: [Validators.required],
      }),
      closedDate: new FormControl(repairRawValue.closedDate),
      budget: new FormControl(repairRawValue.budget),
      workDone: new FormControl(repairRawValue.workDone),
      usedMaterial: new FormControl(repairRawValue.usedMaterial),
      customerMaterial: new FormControl(repairRawValue.customerMaterial),
      importantData: new FormControl(repairRawValue.importantData),
      total: new FormControl(repairRawValue.total),
      device: new FormControl(repairRawValue.device),
      user: new FormControl(repairRawValue.user),
    });
  }

  getRepair(form: RepairFormGroup): IRepair | NewRepair {
    return this.convertRepairRawValueToRepair(form.getRawValue() as RepairFormRawValue | NewRepairFormRawValue);
  }

  resetForm(form: RepairFormGroup, repair: RepairFormGroupInput): void {
    const repairRawValue = this.convertRepairToRepairRawValue({ ...this.getFormDefaults(), ...repair });
    form.reset(
      {
        ...repairRawValue,
        id: { value: repairRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RepairFormDefaults {
    return {
      id: null,
      closedDate: null,
      importantData: false,
    };
  }

  private convertRepairRawValueToRepair(rawRepair: RepairFormRawValue | NewRepairFormRawValue): IRepair | NewRepair {
    return {
      ...rawRepair,
      closedDate: dayjs(rawRepair.closedDate, DATE_TIME_FORMAT),
    };
  }

  private convertRepairToRepairRawValue(
    repair: IRepair | (Partial<NewRepair> & RepairFormDefaults),
  ): RepairFormRawValue | PartialWithRequiredKeyOf<NewRepairFormRawValue> {
    return {
      ...repair,
      closedDate: repair.closedDate ? repair.closedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
