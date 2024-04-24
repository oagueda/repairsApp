import dayjs from 'dayjs/esm';
import { IDevice } from 'app/entities/device/device.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IRepair {
  id: number;
  description?: string | null;
  observations?: string | null;
  internalObservations?: string | null;
  status?: keyof typeof Status | null;
  closedDate?: dayjs.Dayjs | null;
  budget?: string | null;
  workDone?: string | null;
  usedMaterial?: string | null;
  customerMaterial?: string | null;
  importantData?: boolean | null;
  total?: string | null;
  device?: Pick<IDevice, 'id'> | null;
}

export type NewRepair = Omit<IRepair, 'id'> & { id: null };
