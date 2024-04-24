import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'repairsApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'repairsApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'device',
    data: { pageTitle: 'repairsApp.device.home.title' },
    loadChildren: () => import('./device/device.routes'),
  },
  {
    path: 'repair',
    data: { pageTitle: 'repairsApp.repair.home.title' },
    loadChildren: () => import('./repair/repair.routes'),
  },
  {
    path: 'pattern',
    data: { pageTitle: 'repairsApp.pattern.home.title' },
    loadChildren: () => import('./pattern/pattern.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
