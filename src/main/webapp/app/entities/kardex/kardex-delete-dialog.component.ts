import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKardex } from 'app/shared/model/kardex.model';
import { KardexService } from './kardex.service';

@Component({
  templateUrl: './kardex-delete-dialog.component.html',
})
export class KardexDeleteDialogComponent {
  kardex?: IKardex;

  constructor(protected kardexService: KardexService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kardexService.delete(id).subscribe(() => {
      this.eventManager.broadcast('kardexListModification');
      this.activeModal.close();
    });
  }
}
