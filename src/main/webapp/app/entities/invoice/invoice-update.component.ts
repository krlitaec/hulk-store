import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IInvoice, Invoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from './invoice.service';
import { IPayment } from 'app/shared/model/payment.model';
import { PaymentService } from 'app/entities/payment/payment.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IDetailInvoice } from 'app/shared/model/detail-invoice.model';

type SelectableEntity = IPayment | IUser;

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  payments: IPayment[] = [];
  users: IUser[] = [];
  detailInvoices: IDetailInvoice[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    payment: [],
    user: [],
    total: [],
  });

  constructor(
    protected invoiceService: InvoiceService,
    protected paymentService: PaymentService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.detailInvoices = [];
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      if (!invoice.id) {
        const today = moment().startOf('day');
        invoice.date = today;
      }

      this.updateForm(invoice);

      this.paymentService.query().subscribe((res: HttpResponse<IPayment[]>) => (this.payments = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(invoice: IInvoice): void {
    const total = invoice.total || 0;
    this.editForm.patchValue({
      id: invoice.id,
      date: invoice.date ? invoice.date.format(DATE_TIME_FORMAT) : null,
      payment: invoice.payment,
      user: invoice.user,
      total: total.toFixed(2),
    });
    if (invoice.detailInvoices) {
      this.detailInvoices = invoice.detailInvoices;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  private createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      payment: this.editForm.get(['payment'])!.value,
      user: this.editForm.get(['user'])!.value,
      total: 0.0,
      detailInvoices: this.detailInvoices,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  delete(detail: any): void {
    this.detailInvoices = this.detailInvoices.filter(obj => obj !== detail);
  }

  getNameProducto(detail: any): string {
    let name = '';
    if (detail.product) {
      name = detail.product.name;
    }
    return name;
  }
}
