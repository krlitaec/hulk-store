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
    public category?: ICategory
  ) {
    this.enabled = this.enabled || false;
  }
}
