import dayjs from 'dayjs';
import { IEnterprise } from 'app/shared/model/enterprise.model';
import { Pays } from 'app/shared/model/enumerations/pays.model';
import { TypeEmployed } from 'app/shared/model/enumerations/type-employed.model';
import { Department } from 'app/shared/model/enumerations/department.model';
import { Level } from 'app/shared/model/enumerations/level.model';
import { ContractType } from 'app/shared/model/enumerations/contract-type.model';
import { SalaryType } from 'app/shared/model/enumerations/salary-type.model';

export interface IEmployee {
  id?: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string;
  phoneNumber?: string;
  identityCard?: string;
  dateInspiration?: dayjs.Dayjs | null;
  nationality?: keyof typeof Pays | null;
  uploadIdentityCardContentType?: string | null;
  uploadIdentityCard?: string | null;
  typeEmployed?: keyof typeof TypeEmployed | null;
  cityAgency?: string | null;
  residenceCity?: string | null;
  address?: string | null;
  socialSecurityNumber?: string | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  entryDate?: dayjs.Dayjs | null;
  releaseDate?: dayjs.Dayjs | null;
  workstation?: string | null;
  descriptionWorkstation?: string | null;
  department?: keyof typeof Department | null;
  level?: keyof typeof Level | null;
  coefficient?: number | null;
  numberHours?: string | null;
  averageHourlyCost?: string | null;
  monthlyGrossAmount?: number | null;
  commissionAmount?: number | null;
  contractType?: keyof typeof ContractType | null;
  salaryType?: keyof typeof SalaryType | null;
  hireDate?: dayjs.Dayjs | null;
  enterprise?: IEnterprise | null;
  managerEmployee?: IEmployee | null;
}

export const defaultValue: Readonly<IEmployee> = {};
