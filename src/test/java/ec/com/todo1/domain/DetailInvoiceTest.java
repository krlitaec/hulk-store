package ec.com.todo1.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ec.com.todo1.web.rest.TestUtil;

public class DetailInvoiceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailInvoice.class);
        DetailInvoice detailInvoice1 = new DetailInvoice();
        detailInvoice1.setId(1L);
        DetailInvoice detailInvoice2 = new DetailInvoice();
        detailInvoice2.setId(detailInvoice1.getId());
        assertThat(detailInvoice1).isEqualTo(detailInvoice2);
        detailInvoice2.setId(2L);
        assertThat(detailInvoice1).isNotEqualTo(detailInvoice2);
        detailInvoice1.setId(null);
        assertThat(detailInvoice1).isNotEqualTo(detailInvoice2);
    }
}
