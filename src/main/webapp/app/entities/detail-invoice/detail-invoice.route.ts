import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDetailInvoice, DetailInvoice } from 'app/shared/model/detail-invoice.model';
import { DetailInvoiceService } from './detail-invoice.service';
import { DetailInvoiceComponent } from './detail-invoice.component';
import { DetailInvoiceDetailComponent } from './detail-invoice-detail.component';
import { DetailInvoiceUpdateComponent } from './detail-invoice-update.component';

@Injectable({ providedIn: 'root' })
export class DetailInvoiceResolve implements Resolve<IDetailInvoice> {
  constructor(private service: DetailInvoiceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetailInvoice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((detailInvoice: HttpResponse<DetailInvoice>) => {
          if (detailInvoice.body) {
            return of(detailInvoice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DetailInvoice());
  }
}

export const detailInvoiceRoute: Routes = [
  {
    path: '',
    component: DetailInvoiceComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'hulkStoreApp.detailInvoice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetailInvoiceDetailComponent,
    resolve: {
      detailInvoice: DetailInvoiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'hulkStoreApp.detailInvoice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetailInvoiceUpdateComponent,
    resolve: {
      detailInvoice: DetailInvoiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'hulkStoreApp.detailInvoice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetailInvoiceUpdateComponent,
    resolve: {
      detailInvoice: DetailInvoiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'hulkStoreApp.detailInvoice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
