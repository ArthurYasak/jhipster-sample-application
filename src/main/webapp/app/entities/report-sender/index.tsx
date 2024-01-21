import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReportSender from './report-sender';
import ReportSenderDetail from './report-sender-detail';
import ReportSenderUpdate from './report-sender-update';
import ReportSenderDeleteDialog from './report-sender-delete-dialog';

const ReportSenderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReportSender />} />
    <Route path="new" element={<ReportSenderUpdate />} />
    <Route path=":id">
      <Route index element={<ReportSenderDetail />} />
      <Route path="edit" element={<ReportSenderUpdate />} />
      <Route path="delete" element={<ReportSenderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReportSenderRoutes;
