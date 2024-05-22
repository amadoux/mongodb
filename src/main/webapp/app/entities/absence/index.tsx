import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Absence from './absence';
import AbsenceDetail from './absence-detail';
import AbsenceUpdate from './absence-update';
import AbsenceDeleteDialog from './absence-delete-dialog';

const AbsenceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Absence />} />
    <Route path="new" element={<AbsenceUpdate />} />
    <Route path=":id">
      <Route index element={<AbsenceDetail />} />
      <Route path="edit" element={<AbsenceUpdate />} />
      <Route path="delete" element={<AbsenceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AbsenceRoutes;
