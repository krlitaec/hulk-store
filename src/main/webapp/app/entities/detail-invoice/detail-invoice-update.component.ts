import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDetailInvoice, DetailInvoice } from 'app/shared/model/detail-invoice.model';
import { DetailInvoiceService } from './detail-invoice.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from 'app/entities/invoice/invoice.service';

type SelectableEntity = IProduct | IInvoice;

@Component({
  selector: 'jhi-detail-invoice-update',
  templateUrl: './detail-invoice-update.component.html',
})
export class DetailInvoiceUpdateComponent implements OnInit {
  isSaving = false;
  products: IProduct[] = [];
  invoices: IInvoice[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required]],
    price: [null, [Validators.required]],
    total: [null, [Validators.required]],
    product: [],
    invoice: [],
  });

  constructor(
    protected detailInvoiceService: DetailInvoiceService,
    protected productService: ProductService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailInvoice }) => {
      this.updateForm(detailInvoice);

      this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));

      this.invoiceService.query().subscribe((res: HttpResponse<IInvoice[]>) => (this.invoices = res.body || []));
    });
  }

  updateForm(detailInvoice: IDetailInvoice): void {
    this.editForm.patchValue({
      id: detailInvoice.id,
      quantity: detailInvoice.quantity,
      price: detailInvoice.price,
      total: detailInvoice.total,
      product: detailInvoice.product,
      invoice: detailInvoice.invoice,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detailInvoice = this.createFromForm();
    if (detailInvoice.id !== undefined) {
      this.subscribeToSaveResponse(this.detailInvoiceService.update(detailInvoice));
    } else {
      this.subscribeToSaveResponse(this.detailInvoiceService.create(detailInvoice));
    }
  }

  private createFromForm(): IDetailInvoice {
    return {
      ...new DetailInvoice(),
      id: this.editForm.get(['id'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      price: this.editForm.get(['price'])!.value,
      total: this.editForm.get(['total'])!.value,
      product: this.editForm.get(['product'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetailInvoice>>): void {
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
}
