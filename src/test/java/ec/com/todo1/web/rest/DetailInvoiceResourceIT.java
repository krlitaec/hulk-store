package ec.com.todo1.web.rest;

import ec.com.todo1.HulkStoreApp;
import ec.com.todo1.domain.DetailInvoice;
import ec.com.todo1.domain.Product;
import ec.com.todo1.domain.Invoice;
import ec.com.todo1.repository.DetailInvoiceRepository;
import ec.com.todo1.service.DetailInvoiceService;
import ec.com.todo1.service.dto.DetailInvoiceCriteria;
import ec.com.todo1.service.DetailInvoiceQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DetailInvoiceResource} REST controller.
 */
@SpringBootTest(classes = HulkStoreApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DetailInvoiceResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL = new BigDecimal(1 - 1);

    @Autowired
    private DetailInvoiceRepository detailInvoiceRepository;

    @Autowired
    private DetailInvoiceService detailInvoiceService;

    @Autowired
    private DetailInvoiceQueryService detailInvoiceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailInvoiceMockMvc;

    private DetailInvoice detailInvoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailInvoice createEntity(EntityManager em) {
        DetailInvoice detailInvoice = new DetailInvoice()
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .total(DEFAULT_TOTAL);
        return detailInvoice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailInvoice createUpdatedEntity(EntityManager em) {
        DetailInvoice detailInvoice = new DetailInvoice()
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .total(UPDATED_TOTAL);
        return detailInvoice;
    }

    @BeforeEach
    public void initTest() {
        detailInvoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createDetailInvoice() throws Exception {
        int databaseSizeBeforeCreate = detailInvoiceRepository.findAll().size();
        // Create the DetailInvoice
        restDetailInvoiceMockMvc.perform(post("/api/detail-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isCreated());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeCreate + 1);
        DetailInvoice testDetailInvoice = detailInvoiceList.get(detailInvoiceList.size() - 1);
        assertThat(testDetailInvoice.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testDetailInvoice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testDetailInvoice.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    public void createDetailInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = detailInvoiceRepository.findAll().size();

        // Create the DetailInvoice with an existing ID
        detailInvoice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailInvoiceMockMvc.perform(post("/api/detail-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailInvoiceRepository.findAll().size();
        // set the field null
        detailInvoice.setQuantity(null);

        // Create the DetailInvoice, which fails.


        restDetailInvoiceMockMvc.perform(post("/api/detail-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isBadRequest());

        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailInvoiceRepository.findAll().size();
        // set the field null
        detailInvoice.setPrice(null);

        // Create the DetailInvoice, which fails.


        restDetailInvoiceMockMvc.perform(post("/api/detail-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isBadRequest());

        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailInvoiceRepository.findAll().size();
        // set the field null
        detailInvoice.setTotal(null);

        // Create the DetailInvoice, which fails.


        restDetailInvoiceMockMvc.perform(post("/api/detail-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isBadRequest());

        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDetailInvoices() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList
        restDetailInvoiceMockMvc.perform(get("/api/detail-invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }
    
    @Test
    @Transactional
    public void getDetailInvoice() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get the detailInvoice
        restDetailInvoiceMockMvc.perform(get("/api/detail-invoices/{id}", detailInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailInvoice.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }


    @Test
    @Transactional
    public void getDetailInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        Long id = detailInvoice.getId();

        defaultDetailInvoiceShouldBeFound("id.equals=" + id);
        defaultDetailInvoiceShouldNotBeFound("id.notEquals=" + id);

        defaultDetailInvoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDetailInvoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultDetailInvoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDetailInvoiceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDetailInvoicesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where quantity equals to DEFAULT_QUANTITY
        defaultDetailInvoiceShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the detailInvoiceList where quantity equals to UPDATED_QUANTITY
        defaultDetailInvoiceShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where quantity not equals to DEFAULT_QUANTITY
        defaultDetailInvoiceShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the detailInvoiceList where quantity not equals to UPDATED_QUANTITY
        defaultDetailInvoiceShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultDetailInvoiceShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the detailInvoiceList where quantity equals to UPDATED_QUANTITY
        defaultDetailInvoiceShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where quantity is not null
        defaultDetailInvoiceShouldBeFound("quantity.specified=true");

        // Get all the detailInvoiceList where quantity is null
        defaultDetailInvoiceShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultDetailInvoiceShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the detailInvoiceList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultDetailInvoiceShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultDetailInvoiceShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the detailInvoiceList where quantity is less than or equal to SMALLER_QUANTITY
        defaultDetailInvoiceShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where quantity is less than DEFAULT_QUANTITY
        defaultDetailInvoiceShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the detailInvoiceList where quantity is less than UPDATED_QUANTITY
        defaultDetailInvoiceShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where quantity is greater than DEFAULT_QUANTITY
        defaultDetailInvoiceShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the detailInvoiceList where quantity is greater than SMALLER_QUANTITY
        defaultDetailInvoiceShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllDetailInvoicesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where price equals to DEFAULT_PRICE
        defaultDetailInvoiceShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the detailInvoiceList where price equals to UPDATED_PRICE
        defaultDetailInvoiceShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where price not equals to DEFAULT_PRICE
        defaultDetailInvoiceShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the detailInvoiceList where price not equals to UPDATED_PRICE
        defaultDetailInvoiceShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultDetailInvoiceShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the detailInvoiceList where price equals to UPDATED_PRICE
        defaultDetailInvoiceShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where price is not null
        defaultDetailInvoiceShouldBeFound("price.specified=true");

        // Get all the detailInvoiceList where price is null
        defaultDetailInvoiceShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where price is greater than or equal to DEFAULT_PRICE
        defaultDetailInvoiceShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the detailInvoiceList where price is greater than or equal to UPDATED_PRICE
        defaultDetailInvoiceShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where price is less than or equal to DEFAULT_PRICE
        defaultDetailInvoiceShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the detailInvoiceList where price is less than or equal to SMALLER_PRICE
        defaultDetailInvoiceShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where price is less than DEFAULT_PRICE
        defaultDetailInvoiceShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the detailInvoiceList where price is less than UPDATED_PRICE
        defaultDetailInvoiceShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where price is greater than DEFAULT_PRICE
        defaultDetailInvoiceShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the detailInvoiceList where price is greater than SMALLER_PRICE
        defaultDetailInvoiceShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllDetailInvoicesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where total equals to DEFAULT_TOTAL
        defaultDetailInvoiceShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the detailInvoiceList where total equals to UPDATED_TOTAL
        defaultDetailInvoiceShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where total not equals to DEFAULT_TOTAL
        defaultDetailInvoiceShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the detailInvoiceList where total not equals to UPDATED_TOTAL
        defaultDetailInvoiceShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultDetailInvoiceShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the detailInvoiceList where total equals to UPDATED_TOTAL
        defaultDetailInvoiceShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where total is not null
        defaultDetailInvoiceShouldBeFound("total.specified=true");

        // Get all the detailInvoiceList where total is null
        defaultDetailInvoiceShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where total is greater than or equal to DEFAULT_TOTAL
        defaultDetailInvoiceShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the detailInvoiceList where total is greater than or equal to UPDATED_TOTAL
        defaultDetailInvoiceShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where total is less than or equal to DEFAULT_TOTAL
        defaultDetailInvoiceShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the detailInvoiceList where total is less than or equal to SMALLER_TOTAL
        defaultDetailInvoiceShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where total is less than DEFAULT_TOTAL
        defaultDetailInvoiceShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the detailInvoiceList where total is less than UPDATED_TOTAL
        defaultDetailInvoiceShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllDetailInvoicesByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList where total is greater than DEFAULT_TOTAL
        defaultDetailInvoiceShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the detailInvoiceList where total is greater than SMALLER_TOTAL
        defaultDetailInvoiceShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }


    @Test
    @Transactional
    public void getAllDetailInvoicesByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        detailInvoice.setProduct(product);
        detailInvoiceRepository.saveAndFlush(detailInvoice);
        Long productId = product.getId();

        // Get all the detailInvoiceList where product equals to productId
        defaultDetailInvoiceShouldBeFound("productId.equals=" + productId);

        // Get all the detailInvoiceList where product equals to productId + 1
        defaultDetailInvoiceShouldNotBeFound("productId.equals=" + (productId + 1));
    }


    @Test
    @Transactional
    public void getAllDetailInvoicesByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);
        Invoice invoice = InvoiceResourceIT.createEntity(em);
        em.persist(invoice);
        em.flush();
        detailInvoice.setInvoice(invoice);
        detailInvoiceRepository.saveAndFlush(detailInvoice);
        Long invoiceId = invoice.getId();

        // Get all the detailInvoiceList where invoice equals to invoiceId
        defaultDetailInvoiceShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the detailInvoiceList where invoice equals to invoiceId + 1
        defaultDetailInvoiceShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDetailInvoiceShouldBeFound(String filter) throws Exception {
        restDetailInvoiceMockMvc.perform(get("/api/detail-invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));

        // Check, that the count call also returns 1
        restDetailInvoiceMockMvc.perform(get("/api/detail-invoices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDetailInvoiceShouldNotBeFound(String filter) throws Exception {
        restDetailInvoiceMockMvc.perform(get("/api/detail-invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDetailInvoiceMockMvc.perform(get("/api/detail-invoices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingDetailInvoice() throws Exception {
        // Get the detailInvoice
        restDetailInvoiceMockMvc.perform(get("/api/detail-invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDetailInvoice() throws Exception {
        // Initialize the database
        detailInvoiceService.save(detailInvoice);

        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();

        // Update the detailInvoice
        DetailInvoice updatedDetailInvoice = detailInvoiceRepository.findById(detailInvoice.getId()).get();
        // Disconnect from session so that the updates on updatedDetailInvoice are not directly saved in db
        em.detach(updatedDetailInvoice);
        updatedDetailInvoice
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .total(UPDATED_TOTAL);

        restDetailInvoiceMockMvc.perform(put("/api/detail-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDetailInvoice)))
            .andExpect(status().isOk());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
        DetailInvoice testDetailInvoice = detailInvoiceList.get(detailInvoiceList.size() - 1);
        assertThat(testDetailInvoice.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testDetailInvoice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDetailInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingDetailInvoice() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailInvoiceMockMvc.perform(put("/api/detail-invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDetailInvoice() throws Exception {
        // Initialize the database
        detailInvoiceService.save(detailInvoice);

        int databaseSizeBeforeDelete = detailInvoiceRepository.findAll().size();

        // Delete the detailInvoice
        restDetailInvoiceMockMvc.perform(delete("/api/detail-invoices/{id}", detailInvoice.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
