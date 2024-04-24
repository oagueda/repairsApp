import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PatternComponent } from './list/pattern.component';
import { PatternDetailComponent } from './detail/pattern-detail.component';
import { PatternUpdateComponent } from './update/pattern-update.component';
import PatternResolve from './route/pattern-routing-resolve.service';

const patternRoute: Routes = [
  {
    path: '',
    component: PatternComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatternDetailComponent,
    resolve: {
      pattern: PatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PatternUpdateComponent,
    resolve: {
      pattern: PatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatternUpdateComponent,
    resolve: {
      pattern: PatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default patternRoute;
