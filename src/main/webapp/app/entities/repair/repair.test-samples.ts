import dayjs from 'dayjs/esm';

import { IRepair, NewRepair } from './repair.model';

export const sampleWithRequiredData: IRepair = {
  id: 2599,
  description: '../fake-data/blob/hipster.txt',
  status: 'WIP',
};

export const sampleWithPartialData: IRepair = {
  id: 18376,
  description: '../fake-data/blob/hipster.txt',
  internalObservations: '../fake-data/blob/hipster.txt',
  status: 'REVIEW',
  closedDate: dayjs('2024-04-24T02:10'),
  budget: 'infusion',
  usedMaterial: '../fake-data/blob/hipster.txt',
  importantData: true,
  total: 'among coordinated until',
};

export const sampleWithFullData: IRepair = {
  id: 18868,
  description: '../fake-data/blob/hipster.txt',
  observations: '../fake-data/blob/hipster.txt',
  internalObservations: '../fake-data/blob/hipster.txt',
  status: 'WIP',
  closedDate: dayjs('2024-04-23T17:14'),
  budget: 'incidentally sans',
  workDone: '../fake-data/blob/hipster.txt',
  usedMaterial: '../fake-data/blob/hipster.txt',
  customerMaterial: '../fake-data/blob/hipster.txt',
  importantData: false,
  total: 'than drat vacantly',
};

export const sampleWithNewData: NewRepair = {
  description: '../fake-data/blob/hipster.txt',
  status: 'WIP',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
