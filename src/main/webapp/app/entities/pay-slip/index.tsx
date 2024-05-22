import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaySlip from './pay-slip';
import PaySlipDetail from './pay-slip-detail';
import PaySlipUpdate from './pay-slip-update';
import PaySlipDeleteDialog from './pay-slip-delete-dialog';

const PaySlipRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaySlip />} />
    <Route path="new" element={<PaySlipUpdate />} />
    <Route path=":id">
      <Route index element={<PaySlipDetail />} />
      <Route path="edit" element={<PaySlipUpdate />} />
      <Route path="delete" element={<PaySlipDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaySlipRoutes;
