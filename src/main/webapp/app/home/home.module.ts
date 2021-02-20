import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HulkStoreSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { RippleModule } from 'primeng/ripple';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { ToggleButtonModule } from 'primeng/togglebutton';

@NgModule({
  imports: [
    HulkStoreSharedModule,
    RouterModule.forChild([HOME_ROUTE]),
    RippleModule,
    CardModule,
    ButtonModule,
    ScrollPanelModule,
    ToggleButtonModule,
  ],
  declarations: [HomeComponent],
})
export class HulkStoreHomeModule {}
