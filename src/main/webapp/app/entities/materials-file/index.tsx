import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MaterialsFile from './materials-file';
import MaterialsFileDetail from './materials-file-detail';
import MaterialsFileUpdate from './materials-file-update';
import MaterialsFileDeleteDialog from './materials-file-delete-dialog';

const MaterialsFileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MaterialsFile />} />
    <Route path="new" element={<MaterialsFileUpdate />} />
    <Route path=":id">
      <Route index element={<MaterialsFileDetail />} />
      <Route path="edit" element={<MaterialsFileUpdate />} />
      <Route path="delete" element={<MaterialsFileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MaterialsFileRoutes;
