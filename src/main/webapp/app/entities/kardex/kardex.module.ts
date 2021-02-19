import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HulkStoreSharedModule } from 'app/shared/shared.module';
import { KardexComponent } from './kardex.component';
import { KardexDetailComponent } from './kardex-detail.component';
import { KardexUpdateComponent } from './kardex-update.component';
import { KardexDeleteDialogComponent } from './kardex-delete-dialog.component';
import { kardexRoute } from './kardex.route';

@NgModule({
  imports: [HulkStoreSharedModule, RouterModule.forChild(kardexRoute)],
  declarations: [KardexComponent, KardexDetailComponent, KardexUpdateComponent, KardexDeleteDialogComponent],
  entryComponents: [KardexDeleteDialogComponent],
})
export class HulkStoreKardexModule {}
