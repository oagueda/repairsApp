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
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
