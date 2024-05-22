import dayjs from 'dayjs';
import { IEmployee } from 'app/shared/model/employee.model';
import { TypeAbsence } from 'app/shared/model/enumerations/type-absence.model';
import { ConfirmationAbsence } from 'app/shared/model/enumerations/confirmation-absence.model';

export interface IAbsence {
  id?: string;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  numberDayAbsence?: number | null;
  typeAbsence?: keyof typeof TypeAbsence | null;
  confirmationAbsence?: keyof typeof ConfirmationAbsence | null;
  congeRestant?: number | null;
  employee?: IEmployee | null;
}

export const defaultValue: Readonly<IAbsence> = {};
