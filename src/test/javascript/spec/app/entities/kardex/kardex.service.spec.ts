import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { KardexService } from 'app/entities/kardex/kardex.service';
import { IKardex, Kardex } from 'app/shared/model/kardex.model';

describe('Service Tests', () => {
  describe('Kardex Service', () => {
    let injector: TestBed;
    let service: KardexService;
    let httpMock: HttpTestingController;
    let elemDefault: IKardex;
    let expectedResult: IKardex | IKardex[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(KardexService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Kardex(0, 'AAAAAAA', 0, 'AAAAAAA', 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Kardex', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Kardex()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Kardex', () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            quantity: 1,
            comments: 'BBBBBB',
            regularPrice: 1,
            salePrice: 1,
            currentStock: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Kardex', () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            quantity: 1,
            comments: 'BBBBBB',
            regularPrice: 1,
            salePrice: 1,
            currentStock: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Kardex', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
