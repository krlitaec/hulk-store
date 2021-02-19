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
 * Criteria class for the {@link ec.com.todo1.domain.Kardex} entity. This class is used
 * in {@link ec.com.todo1.web.rest.KardexResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /kardexes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class KardexCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private IntegerFilter quantity;

    private StringFilter comments;

    private BigDecimalFilter regularPrice;

    private BigDecimalFilter salePrice;

    private IntegerFilter currentStock;

    private LongFilter productId;

    public KardexCriteria() {
    }

    public KardexCriteria(KardexCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.comments = other.comments == null ? null : other.comments.copy();
        this.regularPrice = other.regularPrice == null ? null : other.regularPrice.copy();
        this.salePrice = other.salePrice == null ? null : other.salePrice.copy();
        this.currentStock = other.currentStock == null ? null : other.currentStock.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
    }

    @Override
    public KardexCriteria copy() {
        return new KardexCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getComments() {
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public BigDecimalFilter getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(BigDecimalFilter regularPrice) {
        this.regularPrice = regularPrice;
    }

    public BigDecimalFilter getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimalFilter salePrice) {
        this.salePrice = salePrice;
    }

    public IntegerFilter getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(IntegerFilter currentStock) {
        this.currentStock = currentStock;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final KardexCriteria that = (KardexCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(regularPrice, that.regularPrice) &&
            Objects.equals(salePrice, that.salePrice) &&
            Objects.equals(currentStock, that.currentStock) &&
            Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        quantity,
        comments,
        regularPrice,
        salePrice,
        currentStock,
        productId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KardexCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (regularPrice != null ? "regularPrice=" + regularPrice + ", " : "") +
                (salePrice != null ? "salePrice=" + salePrice + ", " : "") +
                (currentStock != null ? "currentStock=" + currentStock + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
            "}";
    }

}
