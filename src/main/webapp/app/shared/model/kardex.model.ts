import { IProduct } from 'app/shared/model/product.model';

export interface IKardex {
  id?: number;
  type?: string;
  quantity?: number;
  comments?: string;
  regularPrice?: number;
  salePrice?: number;
  currentStock?: number;
  product?: IProduct;
}

export class Kardex implements IKardex {
  constructor(
    public id?: number,
    public type?: string,
    public quantity?: number,
    public comments?: string,
    public regularPrice?: number,
    public salePrice?: number,
    public currentStock?: number,
    public product?: IProduct
  ) {}
}
