import { ICategory } from 'app/shared/model/category.model';

export interface IProduct {
  id?: number;
  name?: string;
  description?: string;
  enabled?: boolean;
  imageContentType?: string;
  image?: any;
  regularPrice?: number;
  salePrice?: number;
  category?: ICategory;
  added?: boolean;
  quantity?: number;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public enabled?: boolean,
    public imageContentType?: string,
    public image?: any,
    public regularPrice?: number,
    public salePrice?: number,
    public category?: ICategory,
    public added?: boolean,
    public quantity?: number
  ) {
    this.enabled = this.enabled || false;
    this.added = false;
    this.quantity = 1;
  }
}
