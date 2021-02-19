import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKardex } from 'app/shared/model/kardex.model';

@Component({
  selector: 'jhi-kardex-detail',
  templateUrl: './kardex-detail.component.html',
})
export class KardexDetailComponent implements OnInit {
  kardex: IKardex | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kardex }) => (this.kardex = kardex));
  }

  previousState(): void {
    window.history.back();
  }
}
