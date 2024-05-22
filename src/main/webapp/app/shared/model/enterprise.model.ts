import { Pays } from 'app/shared/model/enumerations/pays.model';

export interface IEnterprise {
  id?: string;
  companyName?: string;
  businessRegisterNumber?: string;
  uniqueIdentificationNumber?: string;
  businessDomicile?: string | null;
  email?: string;
  businessPhone?: string;
  country?: keyof typeof Pays | null;
  city?: string | null;
  businessLogoContentType?: string | null;
  businessLogo?: string | null;
  mapLocatorContentType?: string | null;
  mapLocator?: string | null;
}

export const defaultValue: Readonly<IEnterprise> = {};
