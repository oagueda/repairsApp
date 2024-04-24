import { IPattern, NewPattern } from './pattern.model';

export const sampleWithRequiredData: IPattern = {
  id: 29401,
};

export const sampleWithPartialData: IPattern = {
  id: 1676,
  code: 'ew hospitable elk',
};

export const sampleWithFullData: IPattern = {
  id: 23878,
  code: 'brr',
};

export const sampleWithNewData: NewPattern = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
