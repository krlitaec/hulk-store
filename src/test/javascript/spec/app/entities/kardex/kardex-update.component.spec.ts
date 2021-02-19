import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HulkStoreTestModule } from '../../../test.module';
import { KardexUpdateComponent } from 'app/entities/kardex/kardex-update.component';
import { KardexService } from 'app/entities/kardex/kardex.service';
import { Kardex } from 'app/shared/model/kardex.model';

describe('Component Tests', () => {
  describe('Kardex Management Update Component', () => {
    let comp: KardexUpdateComponent;
    let fixture: ComponentFixture<KardexUpdateComponent>;
    let service: KardexService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HulkStoreTestModule],
        declarations: [KardexUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(KardexUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KardexUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KardexService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Kardex(123);
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
        const entity = new Kardex();
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
