export interface IPayment {
  id?: number;
  paymentType?: string;
  value?: number;
  fileContentType?: string;
  file?: any;
}

export class Payment implements IPayment {
  constructor(public id?: number, public paymentType?: string, public value?: number, public fileContentType?: string, public file?: any) {}
}
