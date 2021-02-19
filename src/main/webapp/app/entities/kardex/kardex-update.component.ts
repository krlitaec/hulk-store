import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IKardex, Kardex } from 'app/shared/model/kardex.model';
import { KardexService } from './kardex.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';

@Component({
  selector: 'jhi-kardex-update',
  templateUrl: './kardex-update.component.html',
})
export class KardexUpdateComponent implements OnInit {
  isSaving = false;
  products: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required, Validators.maxLength(1)]],
    quantity: [null, [Validators.required]],
    comments: [null, [Validators.maxLength(500)]],
    regularPrice: [],
    salePrice: [],
    currentStock: [null, [Validators.required]],
    product: [],
  });

  constructor(
    protected kardexService: KardexService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kardex }) => {
      this.updateForm(kardex);

      this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));
    });
  }

  updateForm(kardex: IKardex): void {
    this.editForm.patchValue({
      id: kardex.id,
      type: kardex.type,
      quantity: kardex.quantity,
      comments: kardex.comments,
      regularPrice: kardex.regularPrice,
      salePrice: kardex.salePrice,
      currentStock: kardex.currentStock,
      product: kardex.product,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kardex = this.createFromForm();
    if (kardex.id !== undefined) {
      this.subscribeToSaveResponse(this.kardexService.update(kardex));
    } else {
      this.subscribeToSaveResponse(this.kardexService.create(kardex));
    }
  }

  private createFromForm(): IKardex {
    return {
      ...new Kardex(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      regularPrice: this.editForm.get(['regularPrice'])!.value,
      salePrice: this.editForm.get(['salePrice'])!.value,
      currentStock: this.editForm.get(['currentStock'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKardex>>): void {
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

  trackById(index: number, item: IProduct): any {
    return item.id;
  }
}
