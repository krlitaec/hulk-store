import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HulkStoreSharedModule } from 'app/shared/shared.module';
import { DetailInvoiceComponent } from './detail-invoice.component';
import { DetailInvoiceDetailComponent } from './detail-invoice-detail.component';
import { DetailInvoiceUpdateComponent } from './detail-invoice-update.component';
import { DetailInvoiceDeleteDialogComponent } from './detail-invoice-delete-dialog.component';
import { detailInvoiceRoute } from './detail-invoice.route';

@NgModule({
  imports: [HulkStoreSharedModule, RouterModule.forChild(detailInvoiceRoute)],
  declarations: [DetailInvoiceComponent, DetailInvoiceDetailComponent, DetailInvoiceUpdateComponent, DetailInvoiceDeleteDialogComponent],
  entryComponents: [DetailInvoiceDeleteDialogComponent],
})
export class HulkStoreDetailInvoiceModule {}
