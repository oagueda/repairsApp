import { IDevice, NewDevice } from './device.model';

export const sampleWithRequiredData: IDevice = {
  id: 10146,
  type: 'DESKTOP',
  warranty: true,
  hasPattern: false,
  deleted: true,
};

export const sampleWithPartialData: IDevice = {
  id: 7092,
  type: 'OTHER',
  brand: 'kettle before',
  warranty: false,
  password: 'flavor whereas',
  hasPattern: false,
  deleted: false,
};

export const sampleWithFullData: IDevice = {
  id: 16936,
  type: 'DESKTOP',
  brand: 'accrue timetable likewise',
  model: 'insurrection road',
  serialNumber: 'shirtdress modulo boohoo',
  warranty: true,
  password: 'near circa behind',
  hasPattern: false,
  simPinCode: 'inasmuch reshuffle',
  notes: '../fake-data/blob/hipster.txt',
  deleted: true,
};

export const sampleWithNewData: NewDevice = {
  type: 'PHONE',
  warranty: true,
  hasPattern: false,
  deleted: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
