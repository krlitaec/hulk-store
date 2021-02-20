import { Moment } from 'moment';
import { IPayment } from 'app/shared/model/payment.model';
import { IUser } from 'app/core/user/user.model';
import { IDetailInvoice } from 'app/shared/model/detail-invoice.model';

export interface IInvoice {
  id?: number;
  date?: Moment;
  total?: number;
  payment?: IPayment;
  user?: IUser;
  detailInvoices?: IDetailInvoice[];
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public date?: Moment,
    public total?: number,
    public payment?: IPayment,
    public user?: IUser,
    public detailInvoices?: IDetailInvoice[]
  ) {}
}
