import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.HulkStoreCategoryModule),
      },
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.HulkStoreProductModule),
      },
      {
        path: 'kardex',
        loadChildren: () => import('./kardex/kardex.module').then(m => m.HulkStoreKardexModule),
      },
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.HulkStorePaymentModule),
      },
      {
        path: 'invoice',
        loadChildren: () => import('./invoice/invoice.module').then(m => m.HulkStoreInvoiceModule),
      },
      {
        path: 'detail-invoice',
        loadChildren: () => import('./detail-invoice/detail-invoice.module').then(m => m.HulkStoreDetailInvoiceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class HulkStoreEntityModule {}
