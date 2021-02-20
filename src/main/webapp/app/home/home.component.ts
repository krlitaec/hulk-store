import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { IProduct } from 'app/shared/model/product.model';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { ProductService } from 'app/entities/product/product.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DetailInvoice, IDetailInvoice } from 'app/shared/model/detail-invoice.model';
import { InvoiceService } from 'app/entities/invoice/invoice.service';
import { IInvoice, Invoice } from 'app/shared/model/invoice.model';
import * as moment from 'moment';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  products?: IProduct[];
  categories?: ICategory[];
  category?: ICategory;
  parametersProducts: any;
  page = 0;
  ascending!: boolean;
  ngbPaginationPage = 1;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  invoice?: IInvoice;
  detailInvoices: IDetailInvoice[] = [];

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    protected categoryService: CategoryService,
    protected productService: ProductService,
    protected invoiceService: InvoiceService
  ) {
    this.initSearchParams();
    this.categoryService.query().subscribe(
      (res: HttpResponse<ICategory[]>) => this.onSuccessCategories(res.body),
      () => this.onErrorCategories()
    );
    this.loadInvoice();
  }

  loadInvoice(): void {
    this.invoiceService.findActive().subscribe(
      (res: HttpResponse<IInvoice>) => {
        this.onSuccessInvoiceActive(res.body || new Invoice(0, moment().startOf('day'), 0));
      },
      () => this.onErrorInvoiceActive()
    );
  }

  protected onSuccessInvoiceActive(data: IInvoice): void {
    this.invoice = data;
    if (this.invoice.detailInvoices) {
      this.detailInvoices = this.invoice.detailInvoices;
    }
    if (this.invoice && !this.invoice.id) {
      const today = moment().startOf('day');
      this.invoice.date = today;
    }
  }

  protected onErrorInvoiceActive(): void {
    this.invoice = undefined;
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  protected onSuccessCategories(data: ICategory[] | null): void {
    this.categories = data || [];
  }

  protected onErrorCategories(): void {
    this.categories = [];
  }

  initSearchParams(): void {
    this.parametersProducts = {
      'categoryId.equals': null,
    };
  }

  selectCategory(category: ICategory): void {
    this.category = category;
    this.parametersProducts['categoryId.equals'] = category.id;
    this.queryProducts();
  }

  private queryProducts(): void {
    const parametersProductsAux = Object.assign({}, this.parametersProducts);
    for (const prop in parametersProductsAux) {
      if (!parametersProductsAux[prop]) {
        delete parametersProductsAux[prop];
      }
    }

    const pageToLoad: number = this.page || this.page || 1;
    this.productService
      .query(
        Object.assign(
          {},
          {
            page: this.page - 1,
            size: this.itemsPerPage,
          },
          parametersProductsAux
        )
      )
      .subscribe(
        (res: HttpResponse<IProduct[]>) => this.onSuccessProducts(res.body, res.headers, pageToLoad),
        () => this.onErrorProducts()
      );
  }

  protected onSuccessProducts(data: IProduct[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.products = data || [];
    if (this.products.length !== 0) {
      for (const prod of this.products) {
        prod.added = false;
      }
    }
    if (this.detailInvoices.length !== 0) {
      for (const det of this.detailInvoices) {
        if (det.product !== undefined) {
          for (const prod of this.products) {
            if (det.product.id === prod.id) {
              prod.added = true;
              break;
            }
          }
        }
      }
    }
    this.ngbPaginationPage = this.page;
  }

  protected onErrorProducts(): void {
    this.products = [];
  }

  addRemove(product: IProduct): void {
    if (product.added === true) {
      this.addToCart(product);
    } else {
      this.removeFromCart(product);
    }
  }

  addToCart(product: IProduct): void {
    const detInvoice = new DetailInvoice(0, 1, product.salePrice, 0, product);
    this.detailInvoices.push(detInvoice);
    const invoice = this.invoice || new Invoice(0, moment().startOf('day'), product.salePrice);
    invoice.detailInvoices = this.detailInvoices;
    invoice.total = 0;
    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  removeFromCart(product: IProduct): void {
    let i = 0;
    while (i < this.detailInvoices.length) {
      const det = this.detailInvoices[i];
      if (det.product !== undefined) {
        if (det.product.id === product.id) {
          this.detailInvoices.splice(i, 1);
        } else ++i;
      }
    }
    const invoice = this.invoice || new Invoice(0, moment().startOf('day'), 0);
    invoice.detailInvoices = this.detailInvoices;
    invoice.total = 0;
    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {}

  previousState(): void {
    window.location.reload();
  }
}
