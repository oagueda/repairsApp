import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPattern } from '../pattern.model';
import { PatternService } from '../service/pattern.service';

const patternResolve = (route: ActivatedRouteSnapshot): Observable<null | IPattern> => {
  const id = route.params['id'];
  if (id) {
    return inject(PatternService)
      .find(id)
      .pipe(
        mergeMap((pattern: HttpResponse<IPattern>) => {
          if (pattern.body) {
            return of(pattern.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default patternResolve;
