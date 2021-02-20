import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HulkStoreTestModule } from '../../../test.module';
import { DetailInvoiceUpdateComponent } from 'app/entities/detail-invoice/detail-invoice-update.component';
import { DetailInvoiceService } from 'app/entities/detail-invoice/detail-invoice.service';
import { DetailInvoice } from 'app/shared/model/detail-invoice.model';

describe('Component Tests', () => {
  describe('DetailInvoice Management Update Component', () => {
    let comp: DetailInvoiceUpdateComponent;
    let fixture: ComponentFixture<DetailInvoiceUpdateComponent>;
    let service: DetailInvoiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HulkStoreTestModule],
        declarations: [DetailInvoiceUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DetailInvoiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DetailInvoiceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DetailInvoiceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DetailInvoice(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new DetailInvoice();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
