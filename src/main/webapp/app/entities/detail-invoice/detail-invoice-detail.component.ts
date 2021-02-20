import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetailInvoice } from 'app/shared/model/detail-invoice.model';

@Component({
  selector: 'jhi-detail-invoice-detail',
  templateUrl: './detail-invoice-detail.component.html',
})
export class DetailInvoiceDetailComponent implements OnInit {
  detailInvoice: IDetailInvoice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailInvoice }) => (this.detailInvoice = detailInvoice));
  }

  previousState(): void {
    window.history.back();
  }
}
