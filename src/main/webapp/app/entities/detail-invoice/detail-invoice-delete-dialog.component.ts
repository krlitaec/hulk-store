import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDetailInvoice } from 'app/shared/model/detail-invoice.model';
import { DetailInvoiceService } from './detail-invoice.service';

@Component({
  templateUrl: './detail-invoice-delete-dialog.component.html',
})
export class DetailInvoiceDeleteDialogComponent {
  detailInvoice?: IDetailInvoice;

  constructor(
    protected detailInvoiceService: DetailInvoiceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detailInvoiceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('detailInvoiceListModification');
      this.activeModal.close();
    });
  }
}
