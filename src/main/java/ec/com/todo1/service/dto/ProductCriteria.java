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
 * Criteria class for the {@link ec.com.todo1.domain.Product} entity. This class is used
 * in {@link ec.com.todo1.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private BooleanFilter enabled;

    private BigDecimalFilter regularPrice;

    private BigDecimalFilter salePrice;

    private LongFilter categoryId;

    public ProductCriteria() {
    }

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.regularPrice = other.regularPrice == null ? null : other.regularPrice.copy();
        this.salePrice = other.salePrice == null ? null : other.salePrice.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
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

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(regularPrice, that.regularPrice) &&
            Objects.equals(salePrice, that.salePrice) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        enabled,
        regularPrice,
        salePrice,
        categoryId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (regularPrice != null ? "regularPrice=" + regularPrice + ", " : "") +
                (salePrice != null ? "salePrice=" + salePrice + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            "}";
    }

}
