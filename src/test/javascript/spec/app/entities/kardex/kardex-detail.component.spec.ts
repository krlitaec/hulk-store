import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HulkStoreTestModule } from '../../../test.module';
import { KardexDetailComponent } from 'app/entities/kardex/kardex-detail.component';
import { Kardex } from 'app/shared/model/kardex.model';

describe('Component Tests', () => {
  describe('Kardex Management Detail Component', () => {
    let comp: KardexDetailComponent;
    let fixture: ComponentFixture<KardexDetailComponent>;
    const route = ({ data: of({ kardex: new Kardex(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HulkStoreTestModule],
        declarations: [KardexDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(KardexDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(KardexDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load kardex on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.kardex).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
