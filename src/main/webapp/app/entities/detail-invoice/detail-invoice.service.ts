import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDetailInvoice } from 'app/shared/model/detail-invoice.model';

type EntityResponseType = HttpResponse<IDetailInvoice>;
type EntityArrayResponseType = HttpResponse<IDetailInvoice[]>;

@Injectable({ providedIn: 'root' })
export class DetailInvoiceService {
  public resourceUrl = SERVER_API_URL + 'api/detail-invoices';

  constructor(protected http: HttpClient) {}

  create(detailInvoice: IDetailInvoice): Observable<EntityResponseType> {
    return this.http.post<IDetailInvoice>(this.resourceUrl, detailInvoice, { observe: 'response' });
  }

  update(detailInvoice: IDetailInvoice): Observable<EntityResponseType> {
    return this.http.put<IDetailInvoice>(this.resourceUrl, detailInvoice, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetailInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetailInvoice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
