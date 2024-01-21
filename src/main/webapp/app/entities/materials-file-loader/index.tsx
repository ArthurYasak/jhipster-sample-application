import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MaterialsFileLoader from './materials-file-loader';
import MaterialsFileLoaderDetail from './materials-file-loader-detail';
import MaterialsFileLoaderUpdate from './materials-file-loader-update';
import MaterialsFileLoaderDeleteDialog from './materials-file-loader-delete-dialog';

const MaterialsFileLoaderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MaterialsFileLoader />} />
    <Route path="new" element={<MaterialsFileLoaderUpdate />} />
    <Route path=":id">
      <Route index element={<MaterialsFileLoaderDetail />} />
      <Route path="edit" element={<MaterialsFileLoaderUpdate />} />
      <Route path="delete" element={<MaterialsFileLoaderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MaterialsFileLoaderRoutes;
