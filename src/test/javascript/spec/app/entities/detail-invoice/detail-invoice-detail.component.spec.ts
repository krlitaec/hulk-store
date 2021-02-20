import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HulkStoreTestModule } from '../../../test.module';
import { DetailInvoiceDetailComponent } from 'app/entities/detail-invoice/detail-invoice-detail.component';
import { DetailInvoice } from 'app/shared/model/detail-invoice.model';

describe('Component Tests', () => {
  describe('DetailInvoice Management Detail Component', () => {
    let comp: DetailInvoiceDetailComponent;
    let fixture: ComponentFixture<DetailInvoiceDetailComponent>;
    const route = ({ data: of({ detailInvoice: new DetailInvoice(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HulkStoreTestModule],
        declarations: [DetailInvoiceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DetailInvoiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DetailInvoiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load detailInvoice on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.detailInvoice).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
