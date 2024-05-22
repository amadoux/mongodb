import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Enterprise from './enterprise';
import EnterpriseDetail from './enterprise-detail';
import EnterpriseUpdate from './enterprise-update';
import EnterpriseDeleteDialog from './enterprise-delete-dialog';

const EnterpriseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Enterprise />} />
    <Route path="new" element={<EnterpriseUpdate />} />
    <Route path=":id">
      <Route index element={<EnterpriseDetail />} />
      <Route path="edit" element={<EnterpriseUpdate />} />
      <Route path="delete" element={<EnterpriseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EnterpriseRoutes;
