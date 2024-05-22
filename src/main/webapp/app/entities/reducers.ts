import enterprise from 'app/entities/enterprise/enterprise.reducer';
import employee from 'app/entities/employee/employee.reducer';
import contract from 'app/entities/contract/contract.reducer';
import paySlip from 'app/entities/pay-slip/pay-slip.reducer';
import absence from 'app/entities/absence/absence.reducer';
import socialCharges from 'app/entities/social-charges/social-charges.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  enterprise,
  employee,
  contract,
  paySlip,
  absence,
  socialCharges,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
