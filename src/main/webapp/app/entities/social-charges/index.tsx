import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SocialCharges from './social-charges';
import SocialChargesDetail from './social-charges-detail';
import SocialChargesUpdate from './social-charges-update';
import SocialChargesDeleteDialog from './social-charges-delete-dialog';

const SocialChargesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SocialCharges />} />
    <Route path="new" element={<SocialChargesUpdate />} />
    <Route path=":id">
      <Route index element={<SocialChargesDetail />} />
      <Route path="edit" element={<SocialChargesUpdate />} />
      <Route path="delete" element={<SocialChargesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SocialChargesRoutes;
