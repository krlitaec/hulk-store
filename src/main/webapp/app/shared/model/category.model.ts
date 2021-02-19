export interface ICategory {
  id?: number;
  name?: string;
  code?: string;
  description?: string;
  enabled?: boolean;
  imageContentType?: string;
  image?: any;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string,
    public code?: string,
    public description?: string,
    public enabled?: boolean,
    public imageContentType?: string,
    public image?: any
  ) {
    this.enabled = this.enabled || false;
  }
}
