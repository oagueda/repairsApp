import { IPattern } from 'app/entities/pattern/pattern.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { Type } from 'app/entities/enumerations/type.model';

export interface IDevice {
  id: number;
  type?: keyof typeof Type | null;
  brand?: string | null;
  model?: string | null;
  serialNumber?: string | null;
  warranty?: boolean | null;
  password?: string | null;
  hasPattern?: boolean | null;
  simPinCode?: string | null;
  notes?: string | null;
  deleted?: boolean | null;
  pattern?: IPattern | null;
  customer?: ICustomer | null;
}

export type NewDevice = Omit<IDevice, 'id'> & { id: null };
