package ec.com.todo1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "total", precision = 21, scale = 2, nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JsonIgnoreProperties(value = "invoices", allowSetters = true)
    private Payment payment;

    @ManyToOne
    @JsonIgnoreProperties(value = "invoices", allowSetters = true)
    private User user;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER)
    private Set<DetailInvoice> detailInvoices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public Invoice date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Payment getPayment() {
        return payment;
    }

    public Invoice payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public Invoice user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public Set<DetailInvoice> getDetailInvoices() {
        return detailInvoices;
    }

    public Invoice detailInvoices(Set<DetailInvoice> detailInvoices) {
        this.detailInvoices = detailInvoices;
        return this;
    }

    public Invoice addDetailInvoice(DetailInvoice detailInvoice) {
        this.detailInvoices.add(detailInvoice);
        detailInvoice.setInvoice(this);
        return this;
    }

    public Invoice removeDetailInvoice(DetailInvoice detailInvoice) {
        this.detailInvoices.remove(detailInvoice);
        detailInvoice.setInvoice(null);
        return this;
    }

    public void setDetailInvoices(Set<DetailInvoice> detailInvoices) {
        this.detailInvoices = detailInvoices;
    }
}
