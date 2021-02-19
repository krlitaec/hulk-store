package ec.com.todo1.web.rest;

import ec.com.todo1.HulkStoreApp;
import ec.com.todo1.domain.Kardex;
import ec.com.todo1.domain.Product;
import ec.com.todo1.repository.KardexRepository;
import ec.com.todo1.service.KardexService;
import ec.com.todo1.service.dto.KardexCriteria;
import ec.com.todo1.service.KardexQueryService;

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
 * Integration tests for the {@link KardexResource} REST controller.
 */
@SpringBootTest(classes = HulkStoreApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class KardexResourceIT {

    private static final String DEFAULT_TYPE = "A";
    private static final String UPDATED_TYPE = "B";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_REGULAR_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_REGULAR_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_REGULAR_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SALE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALE_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALE_PRICE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_CURRENT_STOCK = 1;
    private static final Integer UPDATED_CURRENT_STOCK = 2;
    private static final Integer SMALLER_CURRENT_STOCK = 1 - 1;

    @Autowired
    private KardexRepository kardexRepository;

    @Autowired
    private KardexService kardexService;

    @Autowired
    private KardexQueryService kardexQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKardexMockMvc;

    private Kardex kardex;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kardex createEntity(EntityManager em) {
        Kardex kardex = new Kardex()
            .type(DEFAULT_TYPE)
            .quantity(DEFAULT_QUANTITY)
            .comments(DEFAULT_COMMENTS)
            .regularPrice(DEFAULT_REGULAR_PRICE)
            .salePrice(DEFAULT_SALE_PRICE)
            .currentStock(DEFAULT_CURRENT_STOCK);
        return kardex;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kardex createUpdatedEntity(EntityManager em) {
        Kardex kardex = new Kardex()
            .type(UPDATED_TYPE)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS)
            .regularPrice(UPDATED_REGULAR_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .currentStock(UPDATED_CURRENT_STOCK);
        return kardex;
    }

    @BeforeEach
    public void initTest() {
        kardex = createEntity(em);
    }

    @Test
    @Transactional
    public void createKardex() throws Exception {
        int databaseSizeBeforeCreate = kardexRepository.findAll().size();
        // Create the Kardex
        restKardexMockMvc.perform(post("/api/kardexes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kardex)))
            .andExpect(status().isCreated());

        // Validate the Kardex in the database
        List<Kardex> kardexList = kardexRepository.findAll();
        assertThat(kardexList).hasSize(databaseSizeBeforeCreate + 1);
        Kardex testKardex = kardexList.get(kardexList.size() - 1);
        assertThat(testKardex.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testKardex.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testKardex.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testKardex.getRegularPrice()).isEqualTo(DEFAULT_REGULAR_PRICE);
        assertThat(testKardex.getSalePrice()).isEqualTo(DEFAULT_SALE_PRICE);
        assertThat(testKardex.getCurrentStock()).isEqualTo(DEFAULT_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void createKardexWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kardexRepository.findAll().size();

        // Create the Kardex with an existing ID
        kardex.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKardexMockMvc.perform(post("/api/kardexes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kardex)))
            .andExpect(status().isBadRequest());

        // Validate the Kardex in the database
        List<Kardex> kardexList = kardexRepository.findAll();
        assertThat(kardexList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = kardexRepository.findAll().size();
        // set the field null
        kardex.setType(null);

        // Create the Kardex, which fails.


        restKardexMockMvc.perform(post("/api/kardexes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kardex)))
            .andExpect(status().isBadRequest());

        List<Kardex> kardexList = kardexRepository.findAll();
        assertThat(kardexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = kardexRepository.findAll().size();
        // set the field null
        kardex.setQuantity(null);

        // Create the Kardex, which fails.


        restKardexMockMvc.perform(post("/api/kardexes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kardex)))
            .andExpect(status().isBadRequest());

        List<Kardex> kardexList = kardexRepository.findAll();
        assertThat(kardexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = kardexRepository.findAll().size();
        // set the field null
        kardex.setCurrentStock(null);

        // Create the Kardex, which fails.


        restKardexMockMvc.perform(post("/api/kardexes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kardex)))
            .andExpect(status().isBadRequest());

        List<Kardex> kardexList = kardexRepository.findAll();
        assertThat(kardexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKardexes() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList
        restKardexMockMvc.perform(get("/api/kardexes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kardex.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].regularPrice").value(hasItem(DEFAULT_REGULAR_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].currentStock").value(hasItem(DEFAULT_CURRENT_STOCK)));
    }
    
    @Test
    @Transactional
    public void getKardex() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get the kardex
        restKardexMockMvc.perform(get("/api/kardexes/{id}", kardex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kardex.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.regularPrice").value(DEFAULT_REGULAR_PRICE.intValue()))
            .andExpect(jsonPath("$.salePrice").value(DEFAULT_SALE_PRICE.intValue()))
            .andExpect(jsonPath("$.currentStock").value(DEFAULT_CURRENT_STOCK));
    }


    @Test
    @Transactional
    public void getKardexesByIdFiltering() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        Long id = kardex.getId();

        defaultKardexShouldBeFound("id.equals=" + id);
        defaultKardexShouldNotBeFound("id.notEquals=" + id);

        defaultKardexShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultKardexShouldNotBeFound("id.greaterThan=" + id);

        defaultKardexShouldBeFound("id.lessThanOrEqual=" + id);
        defaultKardexShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllKardexesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where type equals to DEFAULT_TYPE
        defaultKardexShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the kardexList where type equals to UPDATED_TYPE
        defaultKardexShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllKardexesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where type not equals to DEFAULT_TYPE
        defaultKardexShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the kardexList where type not equals to UPDATED_TYPE
        defaultKardexShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllKardexesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultKardexShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the kardexList where type equals to UPDATED_TYPE
        defaultKardexShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllKardexesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where type is not null
        defaultKardexShouldBeFound("type.specified=true");

        // Get all the kardexList where type is null
        defaultKardexShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllKardexesByTypeContainsSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where type contains DEFAULT_TYPE
        defaultKardexShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the kardexList where type contains UPDATED_TYPE
        defaultKardexShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllKardexesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where type does not contain DEFAULT_TYPE
        defaultKardexShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the kardexList where type does not contain UPDATED_TYPE
        defaultKardexShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllKardexesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where quantity equals to DEFAULT_QUANTITY
        defaultKardexShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the kardexList where quantity equals to UPDATED_QUANTITY
        defaultKardexShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllKardexesByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where quantity not equals to DEFAULT_QUANTITY
        defaultKardexShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the kardexList where quantity not equals to UPDATED_QUANTITY
        defaultKardexShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllKardexesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultKardexShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the kardexList where quantity equals to UPDATED_QUANTITY
        defaultKardexShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllKardexesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where quantity is not null
        defaultKardexShouldBeFound("quantity.specified=true");

        // Get all the kardexList where quantity is null
        defaultKardexShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllKardexesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultKardexShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the kardexList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultKardexShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllKardexesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultKardexShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the kardexList where quantity is less than or equal to SMALLER_QUANTITY
        defaultKardexShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllKardexesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where quantity is less than DEFAULT_QUANTITY
        defaultKardexShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the kardexList where quantity is less than UPDATED_QUANTITY
        defaultKardexShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllKardexesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where quantity is greater than DEFAULT_QUANTITY
        defaultKardexShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the kardexList where quantity is greater than SMALLER_QUANTITY
        defaultKardexShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllKardexesByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where comments equals to DEFAULT_COMMENTS
        defaultKardexShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the kardexList where comments equals to UPDATED_COMMENTS
        defaultKardexShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllKardexesByCommentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where comments not equals to DEFAULT_COMMENTS
        defaultKardexShouldNotBeFound("comments.notEquals=" + DEFAULT_COMMENTS);

        // Get all the kardexList where comments not equals to UPDATED_COMMENTS
        defaultKardexShouldBeFound("comments.notEquals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllKardexesByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultKardexShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the kardexList where comments equals to UPDATED_COMMENTS
        defaultKardexShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllKardexesByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where comments is not null
        defaultKardexShouldBeFound("comments.specified=true");

        // Get all the kardexList where comments is null
        defaultKardexShouldNotBeFound("comments.specified=false");
    }
                @Test
    @Transactional
    public void getAllKardexesByCommentsContainsSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where comments contains DEFAULT_COMMENTS
        defaultKardexShouldBeFound("comments.contains=" + DEFAULT_COMMENTS);

        // Get all the kardexList where comments contains UPDATED_COMMENTS
        defaultKardexShouldNotBeFound("comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllKardexesByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where comments does not contain DEFAULT_COMMENTS
        defaultKardexShouldNotBeFound("comments.doesNotContain=" + DEFAULT_COMMENTS);

        // Get all the kardexList where comments does not contain UPDATED_COMMENTS
        defaultKardexShouldBeFound("comments.doesNotContain=" + UPDATED_COMMENTS);
    }


    @Test
    @Transactional
    public void getAllKardexesByRegularPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where regularPrice equals to DEFAULT_REGULAR_PRICE
        defaultKardexShouldBeFound("regularPrice.equals=" + DEFAULT_REGULAR_PRICE);

        // Get all the kardexList where regularPrice equals to UPDATED_REGULAR_PRICE
        defaultKardexShouldNotBeFound("regularPrice.equals=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesByRegularPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where regularPrice not equals to DEFAULT_REGULAR_PRICE
        defaultKardexShouldNotBeFound("regularPrice.notEquals=" + DEFAULT_REGULAR_PRICE);

        // Get all the kardexList where regularPrice not equals to UPDATED_REGULAR_PRICE
        defaultKardexShouldBeFound("regularPrice.notEquals=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesByRegularPriceIsInShouldWork() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where regularPrice in DEFAULT_REGULAR_PRICE or UPDATED_REGULAR_PRICE
        defaultKardexShouldBeFound("regularPrice.in=" + DEFAULT_REGULAR_PRICE + "," + UPDATED_REGULAR_PRICE);

        // Get all the kardexList where regularPrice equals to UPDATED_REGULAR_PRICE
        defaultKardexShouldNotBeFound("regularPrice.in=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesByRegularPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where regularPrice is not null
        defaultKardexShouldBeFound("regularPrice.specified=true");

        // Get all the kardexList where regularPrice is null
        defaultKardexShouldNotBeFound("regularPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllKardexesByRegularPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where regularPrice is greater than or equal to DEFAULT_REGULAR_PRICE
        defaultKardexShouldBeFound("regularPrice.greaterThanOrEqual=" + DEFAULT_REGULAR_PRICE);

        // Get all the kardexList where regularPrice is greater than or equal to UPDATED_REGULAR_PRICE
        defaultKardexShouldNotBeFound("regularPrice.greaterThanOrEqual=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesByRegularPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where regularPrice is less than or equal to DEFAULT_REGULAR_PRICE
        defaultKardexShouldBeFound("regularPrice.lessThanOrEqual=" + DEFAULT_REGULAR_PRICE);

        // Get all the kardexList where regularPrice is less than or equal to SMALLER_REGULAR_PRICE
        defaultKardexShouldNotBeFound("regularPrice.lessThanOrEqual=" + SMALLER_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesByRegularPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where regularPrice is less than DEFAULT_REGULAR_PRICE
        defaultKardexShouldNotBeFound("regularPrice.lessThan=" + DEFAULT_REGULAR_PRICE);

        // Get all the kardexList where regularPrice is less than UPDATED_REGULAR_PRICE
        defaultKardexShouldBeFound("regularPrice.lessThan=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesByRegularPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where regularPrice is greater than DEFAULT_REGULAR_PRICE
        defaultKardexShouldNotBeFound("regularPrice.greaterThan=" + DEFAULT_REGULAR_PRICE);

        // Get all the kardexList where regularPrice is greater than SMALLER_REGULAR_PRICE
        defaultKardexShouldBeFound("regularPrice.greaterThan=" + SMALLER_REGULAR_PRICE);
    }


    @Test
    @Transactional
    public void getAllKardexesBySalePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where salePrice equals to DEFAULT_SALE_PRICE
        defaultKardexShouldBeFound("salePrice.equals=" + DEFAULT_SALE_PRICE);

        // Get all the kardexList where salePrice equals to UPDATED_SALE_PRICE
        defaultKardexShouldNotBeFound("salePrice.equals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesBySalePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where salePrice not equals to DEFAULT_SALE_PRICE
        defaultKardexShouldNotBeFound("salePrice.notEquals=" + DEFAULT_SALE_PRICE);

        // Get all the kardexList where salePrice not equals to UPDATED_SALE_PRICE
        defaultKardexShouldBeFound("salePrice.notEquals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesBySalePriceIsInShouldWork() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where salePrice in DEFAULT_SALE_PRICE or UPDATED_SALE_PRICE
        defaultKardexShouldBeFound("salePrice.in=" + DEFAULT_SALE_PRICE + "," + UPDATED_SALE_PRICE);

        // Get all the kardexList where salePrice equals to UPDATED_SALE_PRICE
        defaultKardexShouldNotBeFound("salePrice.in=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesBySalePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where salePrice is not null
        defaultKardexShouldBeFound("salePrice.specified=true");

        // Get all the kardexList where salePrice is null
        defaultKardexShouldNotBeFound("salePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllKardexesBySalePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where salePrice is greater than or equal to DEFAULT_SALE_PRICE
        defaultKardexShouldBeFound("salePrice.greaterThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the kardexList where salePrice is greater than or equal to UPDATED_SALE_PRICE
        defaultKardexShouldNotBeFound("salePrice.greaterThanOrEqual=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesBySalePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where salePrice is less than or equal to DEFAULT_SALE_PRICE
        defaultKardexShouldBeFound("salePrice.lessThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the kardexList where salePrice is less than or equal to SMALLER_SALE_PRICE
        defaultKardexShouldNotBeFound("salePrice.lessThanOrEqual=" + SMALLER_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesBySalePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where salePrice is less than DEFAULT_SALE_PRICE
        defaultKardexShouldNotBeFound("salePrice.lessThan=" + DEFAULT_SALE_PRICE);

        // Get all the kardexList where salePrice is less than UPDATED_SALE_PRICE
        defaultKardexShouldBeFound("salePrice.lessThan=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllKardexesBySalePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where salePrice is greater than DEFAULT_SALE_PRICE
        defaultKardexShouldNotBeFound("salePrice.greaterThan=" + DEFAULT_SALE_PRICE);

        // Get all the kardexList where salePrice is greater than SMALLER_SALE_PRICE
        defaultKardexShouldBeFound("salePrice.greaterThan=" + SMALLER_SALE_PRICE);
    }


    @Test
    @Transactional
    public void getAllKardexesByCurrentStockIsEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where currentStock equals to DEFAULT_CURRENT_STOCK
        defaultKardexShouldBeFound("currentStock.equals=" + DEFAULT_CURRENT_STOCK);

        // Get all the kardexList where currentStock equals to UPDATED_CURRENT_STOCK
        defaultKardexShouldNotBeFound("currentStock.equals=" + UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllKardexesByCurrentStockIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where currentStock not equals to DEFAULT_CURRENT_STOCK
        defaultKardexShouldNotBeFound("currentStock.notEquals=" + DEFAULT_CURRENT_STOCK);

        // Get all the kardexList where currentStock not equals to UPDATED_CURRENT_STOCK
        defaultKardexShouldBeFound("currentStock.notEquals=" + UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllKardexesByCurrentStockIsInShouldWork() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where currentStock in DEFAULT_CURRENT_STOCK or UPDATED_CURRENT_STOCK
        defaultKardexShouldBeFound("currentStock.in=" + DEFAULT_CURRENT_STOCK + "," + UPDATED_CURRENT_STOCK);

        // Get all the kardexList where currentStock equals to UPDATED_CURRENT_STOCK
        defaultKardexShouldNotBeFound("currentStock.in=" + UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllKardexesByCurrentStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where currentStock is not null
        defaultKardexShouldBeFound("currentStock.specified=true");

        // Get all the kardexList where currentStock is null
        defaultKardexShouldNotBeFound("currentStock.specified=false");
    }

    @Test
    @Transactional
    public void getAllKardexesByCurrentStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where currentStock is greater than or equal to DEFAULT_CURRENT_STOCK
        defaultKardexShouldBeFound("currentStock.greaterThanOrEqual=" + DEFAULT_CURRENT_STOCK);

        // Get all the kardexList where currentStock is greater than or equal to UPDATED_CURRENT_STOCK
        defaultKardexShouldNotBeFound("currentStock.greaterThanOrEqual=" + UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllKardexesByCurrentStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where currentStock is less than or equal to DEFAULT_CURRENT_STOCK
        defaultKardexShouldBeFound("currentStock.lessThanOrEqual=" + DEFAULT_CURRENT_STOCK);

        // Get all the kardexList where currentStock is less than or equal to SMALLER_CURRENT_STOCK
        defaultKardexShouldNotBeFound("currentStock.lessThanOrEqual=" + SMALLER_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllKardexesByCurrentStockIsLessThanSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where currentStock is less than DEFAULT_CURRENT_STOCK
        defaultKardexShouldNotBeFound("currentStock.lessThan=" + DEFAULT_CURRENT_STOCK);

        // Get all the kardexList where currentStock is less than UPDATED_CURRENT_STOCK
        defaultKardexShouldBeFound("currentStock.lessThan=" + UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void getAllKardexesByCurrentStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);

        // Get all the kardexList where currentStock is greater than DEFAULT_CURRENT_STOCK
        defaultKardexShouldNotBeFound("currentStock.greaterThan=" + DEFAULT_CURRENT_STOCK);

        // Get all the kardexList where currentStock is greater than SMALLER_CURRENT_STOCK
        defaultKardexShouldBeFound("currentStock.greaterThan=" + SMALLER_CURRENT_STOCK);
    }


    @Test
    @Transactional
    public void getAllKardexesByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        kardexRepository.saveAndFlush(kardex);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        kardex.setProduct(product);
        kardexRepository.saveAndFlush(kardex);
        Long productId = product.getId();

        // Get all the kardexList where product equals to productId
        defaultKardexShouldBeFound("productId.equals=" + productId);

        // Get all the kardexList where product equals to productId + 1
        defaultKardexShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKardexShouldBeFound(String filter) throws Exception {
        restKardexMockMvc.perform(get("/api/kardexes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kardex.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].regularPrice").value(hasItem(DEFAULT_REGULAR_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].currentStock").value(hasItem(DEFAULT_CURRENT_STOCK)));

        // Check, that the count call also returns 1
        restKardexMockMvc.perform(get("/api/kardexes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKardexShouldNotBeFound(String filter) throws Exception {
        restKardexMockMvc.perform(get("/api/kardexes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKardexMockMvc.perform(get("/api/kardexes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingKardex() throws Exception {
        // Get the kardex
        restKardexMockMvc.perform(get("/api/kardexes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKardex() throws Exception {
        // Initialize the database
        kardexService.save(kardex);

        int databaseSizeBeforeUpdate = kardexRepository.findAll().size();

        // Update the kardex
        Kardex updatedKardex = kardexRepository.findById(kardex.getId()).get();
        // Disconnect from session so that the updates on updatedKardex are not directly saved in db
        em.detach(updatedKardex);
        updatedKardex
            .type(UPDATED_TYPE)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS)
            .regularPrice(UPDATED_REGULAR_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .currentStock(UPDATED_CURRENT_STOCK);

        restKardexMockMvc.perform(put("/api/kardexes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedKardex)))
            .andExpect(status().isOk());

        // Validate the Kardex in the database
        List<Kardex> kardexList = kardexRepository.findAll();
        assertThat(kardexList).hasSize(databaseSizeBeforeUpdate);
        Kardex testKardex = kardexList.get(kardexList.size() - 1);
        assertThat(testKardex.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testKardex.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testKardex.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testKardex.getRegularPrice()).isEqualTo(UPDATED_REGULAR_PRICE);
        assertThat(testKardex.getSalePrice()).isEqualTo(UPDATED_SALE_PRICE);
        assertThat(testKardex.getCurrentStock()).isEqualTo(UPDATED_CURRENT_STOCK);
    }

    @Test
    @Transactional
    public void updateNonExistingKardex() throws Exception {
        int databaseSizeBeforeUpdate = kardexRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKardexMockMvc.perform(put("/api/kardexes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kardex)))
            .andExpect(status().isBadRequest());

        // Validate the Kardex in the database
        List<Kardex> kardexList = kardexRepository.findAll();
        assertThat(kardexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKardex() throws Exception {
        // Initialize the database
        kardexService.save(kardex);

        int databaseSizeBeforeDelete = kardexRepository.findAll().size();

        // Delete the kardex
        restKardexMockMvc.perform(delete("/api/kardexes/{id}", kardex.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kardex> kardexList = kardexRepository.findAll();
        assertThat(kardexList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
