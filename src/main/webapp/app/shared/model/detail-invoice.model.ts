import { IProduct } from 'app/shared/model/product.model';
import { IInvoice } from 'app/shared/model/invoice.model';

export interface IDetailInvoice {
  id?: number;
  quantity?: number;
  price?: number;
  total?: number;
  product?: IProduct;
  invoice?: IInvoice;
}

export class DetailInvoice implements IDetailInvoice {
  constructor(
    public id?: number,
    public quantity?: number,
    public price?: number,
    public total?: number,
    public product?: IProduct,
    public invoice?: IInvoice
  ) {}
}
