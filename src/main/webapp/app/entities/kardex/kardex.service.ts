import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IKardex } from 'app/shared/model/kardex.model';

type EntityResponseType = HttpResponse<IKardex>;
type EntityArrayResponseType = HttpResponse<IKardex[]>;

@Injectable({ providedIn: 'root' })
export class KardexService {
  public resourceUrl = SERVER_API_URL + 'api/kardexes';

  constructor(protected http: HttpClient) {}

  create(kardex: IKardex): Observable<EntityResponseType> {
    return this.http.post<IKardex>(this.resourceUrl, kardex, { observe: 'response' });
  }

  update(kardex: IKardex): Observable<EntityResponseType> {
    return this.http.put<IKardex>(this.resourceUrl, kardex, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKardex>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKardex[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
