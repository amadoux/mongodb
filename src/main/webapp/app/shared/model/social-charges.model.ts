import dayjs from 'dayjs';
import { IEmployee } from 'app/shared/model/employee.model';
import { IEnterprise } from 'app/shared/model/enterprise.model';
import { SPentType } from 'app/shared/model/enumerations/s-pent-type.model';
import { StatusCharges } from 'app/shared/model/enumerations/status-charges.model';

export interface ISocialCharges {
  id?: string;
  spentDate?: dayjs.Dayjs | null;
  spentType?: keyof typeof SPentType | null;
  statusCharges?: keyof typeof StatusCharges | null;
  amount?: number;
  commentText?: string;
  responsableDepense?: IEmployee | null;
  enterprise?: IEnterprise | null;
}

export const defaultValue: Readonly<ISocialCharges> = {};
