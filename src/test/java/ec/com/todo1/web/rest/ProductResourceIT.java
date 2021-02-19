package ec.com.todo1.web.rest;

import ec.com.todo1.HulkStoreApp;
import ec.com.todo1.domain.Product;
import ec.com.todo1.domain.Category;
import ec.com.todo1.repository.ProductRepository;
import ec.com.todo1.service.ProductService;
import ec.com.todo1.service.dto.ProductCriteria;
import ec.com.todo1.service.ProductQueryService;

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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@SpringBootTest(classes = HulkStoreApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final BigDecimal DEFAULT_REGULAR_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_REGULAR_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_REGULAR_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SALE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALE_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALE_PRICE = new BigDecimal(1 - 1);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .enabled(DEFAULT_ENABLED)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .regularPrice(DEFAULT_REGULAR_PRICE)
            .salePrice(DEFAULT_SALE_PRICE);
        return product;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .regularPrice(UPDATED_REGULAR_PRICE)
            .salePrice(UPDATED_SALE_PRICE);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getRegularPrice()).isEqualTo(DEFAULT_REGULAR_PRICE);
        assertThat(testProduct.getSalePrice()).isEqualTo(DEFAULT_SALE_PRICE);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.


        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setEnabled(null);

        // Create the Product, which fails.


        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegularPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setRegularPrice(null);

        // Create the Product, which fails.


        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSalePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setSalePrice(null);

        // Create the Product, which fails.


        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].regularPrice").value(hasItem(DEFAULT_REGULAR_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.regularPrice").value(DEFAULT_REGULAR_PRICE.intValue()))
            .andExpect(jsonPath("$.salePrice").value(DEFAULT_SALE_PRICE.intValue()));
    }


    @Test
    @Transactional
    public void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name not equals to DEFAULT_NAME
        defaultProductShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the productList where name not equals to UPDATED_NAME
        defaultProductShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name contains DEFAULT_NAME
        defaultProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productList where name contains UPDATED_NAME
        defaultProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain DEFAULT_NAME
        defaultProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productList where name does not contain UPDATED_NAME
        defaultProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description not equals to DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description not equals to UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllProductsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where enabled equals to DEFAULT_ENABLED
        defaultProductShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the productList where enabled equals to UPDATED_ENABLED
        defaultProductShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllProductsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where enabled not equals to DEFAULT_ENABLED
        defaultProductShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the productList where enabled not equals to UPDATED_ENABLED
        defaultProductShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllProductsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultProductShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the productList where enabled equals to UPDATED_ENABLED
        defaultProductShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllProductsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where enabled is not null
        defaultProductShouldBeFound("enabled.specified=true");

        // Get all the productList where enabled is null
        defaultProductShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByRegularPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where regularPrice equals to DEFAULT_REGULAR_PRICE
        defaultProductShouldBeFound("regularPrice.equals=" + DEFAULT_REGULAR_PRICE);

        // Get all the productList where regularPrice equals to UPDATED_REGULAR_PRICE
        defaultProductShouldNotBeFound("regularPrice.equals=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByRegularPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where regularPrice not equals to DEFAULT_REGULAR_PRICE
        defaultProductShouldNotBeFound("regularPrice.notEquals=" + DEFAULT_REGULAR_PRICE);

        // Get all the productList where regularPrice not equals to UPDATED_REGULAR_PRICE
        defaultProductShouldBeFound("regularPrice.notEquals=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByRegularPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where regularPrice in DEFAULT_REGULAR_PRICE or UPDATED_REGULAR_PRICE
        defaultProductShouldBeFound("regularPrice.in=" + DEFAULT_REGULAR_PRICE + "," + UPDATED_REGULAR_PRICE);

        // Get all the productList where regularPrice equals to UPDATED_REGULAR_PRICE
        defaultProductShouldNotBeFound("regularPrice.in=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByRegularPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where regularPrice is not null
        defaultProductShouldBeFound("regularPrice.specified=true");

        // Get all the productList where regularPrice is null
        defaultProductShouldNotBeFound("regularPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByRegularPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where regularPrice is greater than or equal to DEFAULT_REGULAR_PRICE
        defaultProductShouldBeFound("regularPrice.greaterThanOrEqual=" + DEFAULT_REGULAR_PRICE);

        // Get all the productList where regularPrice is greater than or equal to UPDATED_REGULAR_PRICE
        defaultProductShouldNotBeFound("regularPrice.greaterThanOrEqual=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByRegularPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where regularPrice is less than or equal to DEFAULT_REGULAR_PRICE
        defaultProductShouldBeFound("regularPrice.lessThanOrEqual=" + DEFAULT_REGULAR_PRICE);

        // Get all the productList where regularPrice is less than or equal to SMALLER_REGULAR_PRICE
        defaultProductShouldNotBeFound("regularPrice.lessThanOrEqual=" + SMALLER_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByRegularPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where regularPrice is less than DEFAULT_REGULAR_PRICE
        defaultProductShouldNotBeFound("regularPrice.lessThan=" + DEFAULT_REGULAR_PRICE);

        // Get all the productList where regularPrice is less than UPDATED_REGULAR_PRICE
        defaultProductShouldBeFound("regularPrice.lessThan=" + UPDATED_REGULAR_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByRegularPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where regularPrice is greater than DEFAULT_REGULAR_PRICE
        defaultProductShouldNotBeFound("regularPrice.greaterThan=" + DEFAULT_REGULAR_PRICE);

        // Get all the productList where regularPrice is greater than SMALLER_REGULAR_PRICE
        defaultProductShouldBeFound("regularPrice.greaterThan=" + SMALLER_REGULAR_PRICE);
    }


    @Test
    @Transactional
    public void getAllProductsBySalePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice equals to DEFAULT_SALE_PRICE
        defaultProductShouldBeFound("salePrice.equals=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice equals to UPDATED_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.equals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice not equals to DEFAULT_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.notEquals=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice not equals to UPDATED_SALE_PRICE
        defaultProductShouldBeFound("salePrice.notEquals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice in DEFAULT_SALE_PRICE or UPDATED_SALE_PRICE
        defaultProductShouldBeFound("salePrice.in=" + DEFAULT_SALE_PRICE + "," + UPDATED_SALE_PRICE);

        // Get all the productList where salePrice equals to UPDATED_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.in=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is not null
        defaultProductShouldBeFound("salePrice.specified=true");

        // Get all the productList where salePrice is null
        defaultProductShouldNotBeFound("salePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is greater than or equal to DEFAULT_SALE_PRICE
        defaultProductShouldBeFound("salePrice.greaterThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice is greater than or equal to UPDATED_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.greaterThanOrEqual=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is less than or equal to DEFAULT_SALE_PRICE
        defaultProductShouldBeFound("salePrice.lessThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice is less than or equal to SMALLER_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.lessThanOrEqual=" + SMALLER_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is less than DEFAULT_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.lessThan=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice is less than UPDATED_SALE_PRICE
        defaultProductShouldBeFound("salePrice.lessThan=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is greater than DEFAULT_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.greaterThan=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice is greater than SMALLER_SALE_PRICE
        defaultProductShouldBeFound("salePrice.greaterThan=" + SMALLER_SALE_PRICE);
    }


    @Test
    @Transactional
    public void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        product.setCategory(category);
        productRepository.saveAndFlush(product);
        Long categoryId = category.getId();

        // Get all the productList where category equals to categoryId
        defaultProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productList where category equals to categoryId + 1
        defaultProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].regularPrice").value(hasItem(DEFAULT_REGULAR_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE.intValue())));

        // Check, that the count call also returns 1
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productService.save(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .regularPrice(UPDATED_REGULAR_PRICE)
            .salePrice(UPDATED_SALE_PRICE);

        restProductMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduct)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testProduct.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getRegularPrice()).isEqualTo(UPDATED_REGULAR_PRICE);
        assertThat(testProduct.getSalePrice()).isEqualTo(UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productService.save(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
