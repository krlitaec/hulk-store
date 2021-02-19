package ec.com.todo1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Kardex.
 */
@Entity
@Table(name = "kardex")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Kardex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 1)
    @Column(name = "type", length = 1, nullable = false)
    private String type;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Size(max = 500)
    @Column(name = "comments", length = 500)
    private String comments;

    @Column(name = "regular_price", precision = 21, scale = 2)
    private BigDecimal regularPrice;

    @Column(name = "sale_price", precision = 21, scale = 2)
    private BigDecimal salePrice;

    @NotNull
    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @ManyToOne
    @JsonIgnoreProperties(value = "kardexes", allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Kardex type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Kardex quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public Kardex comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getRegularPrice() {
        return regularPrice;
    }

    public Kardex regularPrice(BigDecimal regularPrice) {
        this.regularPrice = regularPrice;
        return this;
    }

    public void setRegularPrice(BigDecimal regularPrice) {
        this.regularPrice = regularPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public Kardex salePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public Kardex currentStock(Integer currentStock) {
        this.currentStock = currentStock;
        return this;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Product getProduct() {
        return product;
    }

    public Kardex product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kardex)) {
            return false;
        }
        return id != null && id.equals(((Kardex) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Kardex{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", quantity=" + getQuantity() +
            ", comments='" + getComments() + "'" +
            ", regularPrice=" + getRegularPrice() +
            ", salePrice=" + getSalePrice() +
            ", currentStock=" + getCurrentStock() +
            "}";
    }
}
