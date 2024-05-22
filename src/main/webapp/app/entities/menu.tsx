import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/enterprise">
        <Translate contentKey="global.menu.entities.enterprise" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employee">
        <Translate contentKey="global.menu.entities.employee" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/contract">
        <Translate contentKey="global.menu.entities.contract" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pay-slip">
        <Translate contentKey="global.menu.entities.paySlip" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/absence">
        <Translate contentKey="global.menu.entities.absence" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/social-charges">
        <Translate contentKey="global.menu.entities.socialCharges" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
