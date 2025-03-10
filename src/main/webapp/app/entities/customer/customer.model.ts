export interface ICustomer {
  id: number;
  name?: string | null;
  nif?: string | null;
  address?: string | null;
  city?: string | null;
  zipCode?: string | null;
  phone1?: string | null;
  phone2?: string | null;
  email?: string | null;
  deleted?: boolean | null;
  createdBy?: string | null;
  createdDate?: Date | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: Date | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
