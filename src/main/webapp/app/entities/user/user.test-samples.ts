import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 32571,
  login: 'H@NNxHZx\\5T-G',
};

export const sampleWithPartialData: IUser = {
  id: 13186,
  login: 'Px@D7Y\\>M\\uJWZ',
};

export const sampleWithFullData: IUser = {
  id: 26194,
  login: 'hM{@Zu\\GmF\\wMpY\\{M6Gjp',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
