import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDevice, NewDevice } from '../device.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDevice for edit and NewDeviceFormGroupInput for create.
 */
type DeviceFormGroupInput = IDevice | PartialWithRequiredKeyOf<NewDevice>;

type DeviceFormDefaults = Pick<NewDevice, 'id' | 'warranty' | 'hasPattern' | 'deleted'>;

type DeviceFormGroupContent = {
  id: FormControl<IDevice['id'] | NewDevice['id']>;
  type: FormControl<IDevice['type']>;
  brand: FormControl<IDevice['brand']>;
  model: FormControl<IDevice['model']>;
  serialNumber: FormControl<IDevice['serialNumber']>;
  warranty: FormControl<IDevice['warranty']>;
  password: FormControl<IDevice['password']>;
  hasPattern: FormControl<IDevice['hasPattern']>;
  simPinCode: FormControl<IDevice['simPinCode']>;
  notes: FormControl<IDevice['notes']>;
  deleted: FormControl<IDevice['deleted']>;
  pattern: FormControl<IDevice['pattern']>;
  customer: FormControl<IDevice['customer']>;
};

export type DeviceFormGroup = FormGroup<DeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceFormService {
  createDeviceFormGroup(device: DeviceFormGroupInput = { id: null }): DeviceFormGroup {
    const deviceRawValue = {
      ...this.getFormDefaults(),
      ...device,
    };
    return new FormGroup<DeviceFormGroupContent>({
      id: new FormControl(
        { value: deviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(deviceRawValue.type, {
        validators: [Validators.required],
      }),
      brand: new FormControl(deviceRawValue.brand),
      model: new FormControl(deviceRawValue.model),
      serialNumber: new FormControl(deviceRawValue.serialNumber),
      warranty: new FormControl(deviceRawValue.warranty, {
        validators: [Validators.required],
      }),
      password: new FormControl(deviceRawValue.password),
      hasPattern: new FormControl(deviceRawValue.hasPattern, {
        validators: [Validators.required],
      }),
      simPinCode: new FormControl(deviceRawValue.simPinCode),
      notes: new FormControl(deviceRawValue.notes),
      deleted: new FormControl(deviceRawValue.deleted, {
        validators: [Validators.required],
      }),
      pattern: new FormControl(deviceRawValue.pattern),
      customer: new FormControl(deviceRawValue.customer),
    });
  }

  getDevice(form: DeviceFormGroup): IDevice | NewDevice {
    return form.getRawValue() as IDevice | NewDevice;
  }

  resetForm(form: DeviceFormGroup, device: DeviceFormGroupInput): void {
    const deviceRawValue = { ...this.getFormDefaults(), ...device };
    form.reset(
      {
        ...deviceRawValue,
        id: { value: deviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeviceFormDefaults {
    return {
      id: null,
      warranty: false,
      hasPattern: false,
      deleted: false,
    };
  }
}
