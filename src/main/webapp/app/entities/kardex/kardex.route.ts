import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IKardex, Kardex } from 'app/shared/model/kardex.model';
import { KardexService } from './kardex.service';
import { KardexComponent } from './kardex.component';
import { KardexDetailComponent } from './kardex-detail.component';
import { KardexUpdateComponent } from './kardex-update.component';

@Injectable({ providedIn: 'root' })
export class KardexResolve implements Resolve<IKardex> {
  constructor(private service: KardexService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKardex> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((kardex: HttpResponse<Kardex>) => {
          if (kardex.body) {
            return of(kardex.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Kardex());
  }
}

export const kardexRoute: Routes = [
  {
    path: '',
    component: KardexComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'hulkStoreApp.kardex.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KardexDetailComponent,
    resolve: {
      kardex: KardexResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'hulkStoreApp.kardex.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KardexUpdateComponent,
    resolve: {
      kardex: KardexResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'hulkStoreApp.kardex.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KardexUpdateComponent,
    resolve: {
      kardex: KardexResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'hulkStoreApp.kardex.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
