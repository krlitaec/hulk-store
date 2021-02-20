package ec.com.todo1.web.rest;

import ec.com.todo1.HulkStoreApp;
import ec.com.todo1.domain.Payment;
import ec.com.todo1.repository.PaymentRepository;
import ec.com.todo1.service.PaymentService;
import ec.com.todo1.service.dto.PaymentCriteria;
import ec.com.todo1.service.PaymentQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@SpringBootTest(classes = HulkStoreApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaymentResourceIT {

    private static final String DEFAULT_PAYMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE = new BigDecimal(1 - 1);

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentQueryService paymentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .value(DEFAULT_VALUE)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE);
        return payment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .paymentType(UPDATED_PAYMENT_TYPE)
            .value(UPDATED_VALUE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testPayment.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testPayment.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testPayment.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment with an existing ID
        payment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPaymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setPaymentType(null);

        // Create the Payment, which fails.


        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setValue(null);

        // Create the Payment, which fails.


        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }
    
    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
    }


    @Test
    @Transactional
    public void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentShouldBeFound("id.equals=" + id);
        defaultPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentsByPaymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentType equals to DEFAULT_PAYMENT_TYPE
        defaultPaymentShouldBeFound("paymentType.equals=" + DEFAULT_PAYMENT_TYPE);

        // Get all the paymentList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultPaymentShouldNotBeFound("paymentType.equals=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentType not equals to DEFAULT_PAYMENT_TYPE
        defaultPaymentShouldNotBeFound("paymentType.notEquals=" + DEFAULT_PAYMENT_TYPE);

        // Get all the paymentList where paymentType not equals to UPDATED_PAYMENT_TYPE
        defaultPaymentShouldBeFound("paymentType.notEquals=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentType in DEFAULT_PAYMENT_TYPE or UPDATED_PAYMENT_TYPE
        defaultPaymentShouldBeFound("paymentType.in=" + DEFAULT_PAYMENT_TYPE + "," + UPDATED_PAYMENT_TYPE);

        // Get all the paymentList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultPaymentShouldNotBeFound("paymentType.in=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentType is not null
        defaultPaymentShouldBeFound("paymentType.specified=true");

        // Get all the paymentList where paymentType is null
        defaultPaymentShouldNotBeFound("paymentType.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentsByPaymentTypeContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentType contains DEFAULT_PAYMENT_TYPE
        defaultPaymentShouldBeFound("paymentType.contains=" + DEFAULT_PAYMENT_TYPE);

        // Get all the paymentList where paymentType contains UPDATED_PAYMENT_TYPE
        defaultPaymentShouldNotBeFound("paymentType.contains=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentType does not contain DEFAULT_PAYMENT_TYPE
        defaultPaymentShouldNotBeFound("paymentType.doesNotContain=" + DEFAULT_PAYMENT_TYPE);

        // Get all the paymentList where paymentType does not contain UPDATED_PAYMENT_TYPE
        defaultPaymentShouldBeFound("paymentType.doesNotContain=" + UPDATED_PAYMENT_TYPE);
    }


    @Test
    @Transactional
    public void getAllPaymentsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where value equals to DEFAULT_VALUE
        defaultPaymentShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the paymentList where value equals to UPDATED_VALUE
        defaultPaymentShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where value not equals to DEFAULT_VALUE
        defaultPaymentShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the paymentList where value not equals to UPDATED_VALUE
        defaultPaymentShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultPaymentShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the paymentList where value equals to UPDATED_VALUE
        defaultPaymentShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where value is not null
        defaultPaymentShouldBeFound("value.specified=true");

        // Get all the paymentList where value is null
        defaultPaymentShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where value is greater than or equal to DEFAULT_VALUE
        defaultPaymentShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the paymentList where value is greater than or equal to UPDATED_VALUE
        defaultPaymentShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where value is less than or equal to DEFAULT_VALUE
        defaultPaymentShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the paymentList where value is less than or equal to SMALLER_VALUE
        defaultPaymentShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where value is less than DEFAULT_VALUE
        defaultPaymentShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the paymentList where value is less than UPDATED_VALUE
        defaultPaymentShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where value is greater than DEFAULT_VALUE
        defaultPaymentShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the paymentList where value is greater than SMALLER_VALUE
        defaultPaymentShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));

        // Check, that the count call also returns 1
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentService.save(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .paymentType(UPDATED_PAYMENT_TYPE)
            .value(UPDATED_VALUE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayment)))
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPayment.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testPayment.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testPayment.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentService.save(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
