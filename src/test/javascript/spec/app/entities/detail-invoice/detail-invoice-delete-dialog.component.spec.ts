import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HulkStoreTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { DetailInvoiceDeleteDialogComponent } from 'app/entities/detail-invoice/detail-invoice-delete-dialog.component';
import { DetailInvoiceService } from 'app/entities/detail-invoice/detail-invoice.service';

describe('Component Tests', () => {
  describe('DetailInvoice Management Delete Component', () => {
    let comp: DetailInvoiceDeleteDialogComponent;
    let fixture: ComponentFixture<DetailInvoiceDeleteDialogComponent>;
    let service: DetailInvoiceService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HulkStoreTestModule],
        declarations: [DetailInvoiceDeleteDialogComponent],
      })
        .overrideTemplate(DetailInvoiceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DetailInvoiceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DetailInvoiceService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
