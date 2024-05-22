import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Enterprise from './enterprise';
import Employee from './employee';
import Contract from './contract';
import PaySlip from './pay-slip';
import Absence from './absence';
import SocialCharges from './social-charges';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="enterprise/*" element={<Enterprise />} />
        <Route path="employee/*" element={<Employee />} />
        <Route path="contract/*" element={<Contract />} />
        <Route path="pay-slip/*" element={<PaySlip />} />
        <Route path="absence/*" element={<Absence />} />
        <Route path="social-charges/*" element={<SocialCharges />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
