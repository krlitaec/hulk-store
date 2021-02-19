package ec.com.todo1.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ec.com.todo1.web.rest.TestUtil;

public class KardexTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kardex.class);
        Kardex kardex1 = new Kardex();
        kardex1.setId(1L);
        Kardex kardex2 = new Kardex();
        kardex2.setId(kardex1.getId());
        assertThat(kardex1).isEqualTo(kardex2);
        kardex2.setId(2L);
        assertThat(kardex1).isNotEqualTo(kardex2);
        kardex1.setId(null);
        assertThat(kardex1).isNotEqualTo(kardex2);
    }
}
