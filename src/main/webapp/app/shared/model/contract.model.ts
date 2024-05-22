import dayjs from 'dayjs';
import { IEmployee } from 'app/shared/model/employee.model';
import { ContractType } from 'app/shared/model/enumerations/contract-type.model';
import { StatusContract } from 'app/shared/model/enumerations/status-contract.model';

export interface IContract {
  id?: string;
  contractType?: keyof typeof ContractType | null;
  entryDate?: dayjs.Dayjs | null;
  releaseDate?: dayjs.Dayjs | null;
  statusContract?: keyof typeof StatusContract | null;
  uploadContractContentType?: string;
  uploadContract?: string;
  employee?: IEmployee | null;
}

export const defaultValue: Readonly<IContract> = {};
