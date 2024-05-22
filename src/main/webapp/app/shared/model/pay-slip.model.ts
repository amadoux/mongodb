import dayjs from 'dayjs';
import { IEmployee } from 'app/shared/model/employee.model';

export interface IPaySlip {
  id?: string;
  netSalaryPay?: number | null;
  paySlipDate?: dayjs.Dayjs | null;
  uploadPaySlipContentType?: string | null;
  uploadPaySlip?: string | null;
  employee?: IEmployee | null;
}

export const defaultValue: Readonly<IPaySlip> = {};
