import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 148,
  name: 'appropriate',
  nif: 'josh',
  phone1: 'healthily repudiate strictly',
  deleted: false,
};

export const sampleWithPartialData: ICustomer = {
  id: 31981,
  name: 'delivery step',
  nif: 'reorient',
  city: 'Delray Beach',
  phone1: 'knowledgeably',
  email: 'Lazaro.Pollich26@hotmail.com',
  deleted: false,
};

export const sampleWithFullData: ICustomer = {
  id: 31153,
  name: 'below accidentally seemingly',
  nif: 'lathe even',
  address: 'before over',
  city: 'West Kimberly',
  zipCode: '45120-3711',
  phone1: 'consequently brace',
  phone2: 'similar vivaciously because',
  email: 'Gennaro_Leffler39@gmail.com',
  deleted: true,
};

export const sampleWithNewData: NewCustomer = {
  name: 'as',
  nif: 'fast multicolored gleefully',
  phone1: 'disagreement flickering pace',
  deleted: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
