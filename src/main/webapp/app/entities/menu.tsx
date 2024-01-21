import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/materials-file">
        <Translate contentKey="global.menu.entities.materialsFile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/materials-file-loader">
        <Translate contentKey="global.menu.entities.materialsFileLoader" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/theme-file">
        <Translate contentKey="global.menu.entities.themeFile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/theme-file-creator">
        <Translate contentKey="global.menu.entities.themeFileCreator" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-entity">
        <Translate contentKey="global.menu.entities.testEntity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-loader">
        <Translate contentKey="global.menu.entities.testLoader" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-creator">
        <Translate contentKey="global.menu.entities.testCreator" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tester">
        <Translate contentKey="global.menu.entities.tester" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-user">
        <Translate contentKey="global.menu.entities.testUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/report-sender">
        <Translate contentKey="global.menu.entities.reportSender" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/statistic-generator">
        <Translate contentKey="global.menu.entities.statisticGenerator" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
