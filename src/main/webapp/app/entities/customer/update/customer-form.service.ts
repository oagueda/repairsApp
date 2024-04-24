import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id' | 'deleted'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  name: FormControl<ICustomer['name']>;
  nif: FormControl<ICustomer['nif']>;
  address: FormControl<ICustomer['address']>;
  city: FormControl<ICustomer['city']>;
  zipCode: FormControl<ICustomer['zipCode']>;
  phone1: FormControl<ICustomer['phone1']>;
  phone2: FormControl<ICustomer['phone2']>;
  email: FormControl<ICustomer['email']>;
  deleted: FormControl<ICustomer['deleted']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = {
      ...this.getFormDefaults(),
      ...customer,
    };
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(customerRawValue.name, {
        validators: [Validators.required],
      }),
      nif: new FormControl(customerRawValue.nif, {
        validators: [Validators.required],
      }),
      address: new FormControl(customerRawValue.address),
      city: new FormControl(customerRawValue.city),
      zipCode: new FormControl(customerRawValue.zipCode),
      phone1: new FormControl(customerRawValue.phone1, {
        validators: [Validators.required],
      }),
      phone2: new FormControl(customerRawValue.phone2),
      email: new FormControl(customerRawValue.email),
      deleted: new FormControl(customerRawValue.deleted, {
        validators: [Validators.required],
      }),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return form.getRawValue() as ICustomer | NewCustomer;
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = { ...this.getFormDefaults(), ...customer };
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    return {
      id: null,
      deleted: false,
    };
  }
}
