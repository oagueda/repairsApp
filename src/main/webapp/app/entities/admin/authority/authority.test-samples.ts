import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '7563de17-1264-460e-93af-b60ab50bfe2c',
};

export const sampleWithPartialData: IAuthority = {
  name: '472b4c51-2e06-466c-b967-51e20341cd45',
};

export const sampleWithFullData: IAuthority = {
  name: '791c60a0-3244-4317-930f-b9eeca20a767',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
