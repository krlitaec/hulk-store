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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class HulkStoreEntityModule {}
