package ec.com.todo1.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link ec.com.todo1.domain.DetailInvoice} entity. This class is used
 * in {@link ec.com.todo1.web.rest.DetailInvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /detail-invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DetailInvoiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quantity;

    private BigDecimalFilter price;

    private BigDecimalFilter total;

    private LongFilter productId;

    private LongFilter invoiceId;

    public DetailInvoiceCriteria() {
    }

    public DetailInvoiceCriteria(DetailInvoiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.invoiceId = other.invoiceId == null ? null : other.invoiceId.copy();
    }

    @Override
    public DetailInvoiceCriteria copy() {
        return new DetailInvoiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public BigDecimalFilter getTotal() {
        return total;
    }

    public void setTotal(BigDecimalFilter total) {
        this.total = total;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DetailInvoiceCriteria that = (DetailInvoiceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(price, that.price) &&
            Objects.equals(total, that.total) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(invoiceId, that.invoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        quantity,
        price,
        total,
        productId,
        invoiceId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailInvoiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (total != null ? "total=" + total + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
                (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
            "}";
    }

}
