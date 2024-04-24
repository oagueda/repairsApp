package xyz.oagueda.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.oagueda.domain.DeviceAsserts.*;
import static xyz.oagueda.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.oagueda.IntegrationTest;
import xyz.oagueda.domain.Customer;
import xyz.oagueda.domain.Device;
import xyz.oagueda.domain.Pattern;
import xyz.oagueda.domain.enumeration.Type;
import xyz.oagueda.repository.DeviceRepository;
import xyz.oagueda.service.dto.DeviceDTO;
import xyz.oagueda.service.mapper.DeviceMapper;

/**
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final Type DEFAULT_TYPE = Type.LAPTOP;
    private static final Type UPDATED_TYPE = Type.DESKTOP;

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WARRANTY = false;
    private static final Boolean UPDATED_WARRANTY = true;

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_PATTERN = false;
    private static final Boolean UPDATED_HAS_PATTERN = true;

    private static final String DEFAULT_SIM_PIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SIM_PIN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity(EntityManager em) {
        Device device = new Device()
            .type(DEFAULT_TYPE)
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .warranty(DEFAULT_WARRANTY)
            .password(DEFAULT_PASSWORD)
            .hasPattern(DEFAULT_HAS_PATTERN)
            .simPinCode(DEFAULT_SIM_PIN_CODE)
            .notes(DEFAULT_NOTES)
            .deleted(DEFAULT_DELETED);
        return device;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity(EntityManager em) {
        Device device = new Device()
            .type(UPDATED_TYPE)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .warranty(UPDATED_WARRANTY)
            .password(UPDATED_PASSWORD)
            .hasPattern(UPDATED_HAS_PATTERN)
            .simPinCode(UPDATED_SIM_PIN_CODE)
            .notes(UPDATED_NOTES)
            .deleted(UPDATED_DELETED);
        return device;
    }

    @BeforeEach
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        var returnedDeviceDTO = om.readValue(
            restDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DeviceDTO.class
        );

        // Validate the Device in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDevice = deviceMapper.toEntity(returnedDeviceDTO);
        assertDeviceUpdatableFieldsEquals(returnedDevice, getPersistedDevice(returnedDevice));
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId(1L);
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setType(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWarrantyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setWarranty(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHasPatternIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setHasPattern(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setDeleted(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].warranty").value(hasItem(DEFAULT_WARRANTY.booleanValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].hasPattern").value(hasItem(DEFAULT_HAS_PATTERN.booleanValue())))
            .andExpect(jsonPath("$.[*].simPinCode").value(hasItem(DEFAULT_SIM_PIN_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.warranty").value(DEFAULT_WARRANTY.booleanValue()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.hasPattern").value(DEFAULT_HAS_PATTERN.booleanValue()))
            .andExpect(jsonPath("$.simPinCode").value(DEFAULT_SIM_PIN_CODE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getDevicesByIdFiltering() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        Long id = device.getId();

        defaultDeviceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDeviceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDeviceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDevicesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where type equals to
        defaultDeviceFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDevicesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where type in
        defaultDeviceFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDevicesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where type is not null
        defaultDeviceFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where brand equals to
        defaultDeviceFiltering("brand.equals=" + DEFAULT_BRAND, "brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllDevicesByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where brand in
        defaultDeviceFiltering("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND, "brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllDevicesByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where brand is not null
        defaultDeviceFiltering("brand.specified=true", "brand.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByBrandContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where brand contains
        defaultDeviceFiltering("brand.contains=" + DEFAULT_BRAND, "brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllDevicesByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where brand does not contain
        defaultDeviceFiltering("brand.doesNotContain=" + UPDATED_BRAND, "brand.doesNotContain=" + DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void getAllDevicesByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where model equals to
        defaultDeviceFiltering("model.equals=" + DEFAULT_MODEL, "model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllDevicesByModelIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where model in
        defaultDeviceFiltering("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL, "model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllDevicesByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where model is not null
        defaultDeviceFiltering("model.specified=true", "model.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByModelContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where model contains
        defaultDeviceFiltering("model.contains=" + DEFAULT_MODEL, "model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllDevicesByModelNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where model does not contain
        defaultDeviceFiltering("model.doesNotContain=" + UPDATED_MODEL, "model.doesNotContain=" + DEFAULT_MODEL);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber equals to
        defaultDeviceFiltering("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER, "serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber in
        defaultDeviceFiltering(
            "serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER,
            "serialNumber.in=" + UPDATED_SERIAL_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber is not null
        defaultDeviceFiltering("serialNumber.specified=true", "serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber contains
        defaultDeviceFiltering("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER, "serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber does not contain
        defaultDeviceFiltering(
            "serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER,
            "serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDevicesByWarrantyIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where warranty equals to
        defaultDeviceFiltering("warranty.equals=" + DEFAULT_WARRANTY, "warranty.equals=" + UPDATED_WARRANTY);
    }

    @Test
    @Transactional
    void getAllDevicesByWarrantyIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where warranty in
        defaultDeviceFiltering("warranty.in=" + DEFAULT_WARRANTY + "," + UPDATED_WARRANTY, "warranty.in=" + UPDATED_WARRANTY);
    }

    @Test
    @Transactional
    void getAllDevicesByWarrantyIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where warranty is not null
        defaultDeviceFiltering("warranty.specified=true", "warranty.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where password equals to
        defaultDeviceFiltering("password.equals=" + DEFAULT_PASSWORD, "password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllDevicesByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where password in
        defaultDeviceFiltering("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD, "password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllDevicesByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where password is not null
        defaultDeviceFiltering("password.specified=true", "password.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByPasswordContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where password contains
        defaultDeviceFiltering("password.contains=" + DEFAULT_PASSWORD, "password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllDevicesByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where password does not contain
        defaultDeviceFiltering("password.doesNotContain=" + UPDATED_PASSWORD, "password.doesNotContain=" + DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllDevicesByHasPatternIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where hasPattern equals to
        defaultDeviceFiltering("hasPattern.equals=" + DEFAULT_HAS_PATTERN, "hasPattern.equals=" + UPDATED_HAS_PATTERN);
    }

    @Test
    @Transactional
    void getAllDevicesByHasPatternIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where hasPattern in
        defaultDeviceFiltering("hasPattern.in=" + DEFAULT_HAS_PATTERN + "," + UPDATED_HAS_PATTERN, "hasPattern.in=" + UPDATED_HAS_PATTERN);
    }

    @Test
    @Transactional
    void getAllDevicesByHasPatternIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where hasPattern is not null
        defaultDeviceFiltering("hasPattern.specified=true", "hasPattern.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesBySimPinCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where simPinCode equals to
        defaultDeviceFiltering("simPinCode.equals=" + DEFAULT_SIM_PIN_CODE, "simPinCode.equals=" + UPDATED_SIM_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesBySimPinCodeIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where simPinCode in
        defaultDeviceFiltering(
            "simPinCode.in=" + DEFAULT_SIM_PIN_CODE + "," + UPDATED_SIM_PIN_CODE,
            "simPinCode.in=" + UPDATED_SIM_PIN_CODE
        );
    }

    @Test
    @Transactional
    void getAllDevicesBySimPinCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where simPinCode is not null
        defaultDeviceFiltering("simPinCode.specified=true", "simPinCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesBySimPinCodeContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where simPinCode contains
        defaultDeviceFiltering("simPinCode.contains=" + DEFAULT_SIM_PIN_CODE, "simPinCode.contains=" + UPDATED_SIM_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesBySimPinCodeNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where simPinCode does not contain
        defaultDeviceFiltering("simPinCode.doesNotContain=" + UPDATED_SIM_PIN_CODE, "simPinCode.doesNotContain=" + DEFAULT_SIM_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllDevicesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deleted equals to
        defaultDeviceFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllDevicesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deleted in
        defaultDeviceFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllDevicesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deleted is not null
        defaultDeviceFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByPatternIsEqualToSomething() throws Exception {
        Pattern pattern;
        if (TestUtil.findAll(em, Pattern.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            pattern = PatternResourceIT.createEntity(em);
        } else {
            pattern = TestUtil.findAll(em, Pattern.class).get(0);
        }
        em.persist(pattern);
        em.flush();
        device.setPattern(pattern);
        deviceRepository.saveAndFlush(device);
        Long patternId = pattern.getId();
        // Get all the deviceList where pattern equals to patternId
        defaultDeviceShouldBeFound("patternId.equals=" + patternId);

        // Get all the deviceList where pattern equals to (patternId + 1)
        defaultDeviceShouldNotBeFound("patternId.equals=" + (patternId + 1));
    }

    @Test
    @Transactional
    void getAllDevicesByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        device.setCustomer(customer);
        deviceRepository.saveAndFlush(device);
        Long customerId = customer.getId();
        // Get all the deviceList where customer equals to customerId
        defaultDeviceShouldBeFound("customerId.equals=" + customerId);

        // Get all the deviceList where customer equals to (customerId + 1)
        defaultDeviceShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultDeviceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDeviceShouldBeFound(shouldBeFound);
        defaultDeviceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceShouldBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].warranty").value(hasItem(DEFAULT_WARRANTY.booleanValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].hasPattern").value(hasItem(DEFAULT_HAS_PATTERN.booleanValue())))
            .andExpect(jsonPath("$.[*].simPinCode").value(hasItem(DEFAULT_SIM_PIN_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceShouldNotBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice
            .type(UPDATED_TYPE)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .warranty(UPDATED_WARRANTY)
            .password(UPDATED_PASSWORD)
            .hasPattern(UPDATED_HAS_PATTERN)
            .simPinCode(UPDATED_SIM_PIN_CODE)
            .notes(UPDATED_NOTES)
            .deleted(UPDATED_DELETED);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeviceToMatchAllProperties(updatedDevice);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .warranty(UPDATED_WARRANTY)
            .simPinCode(UPDATED_SIM_PIN_CODE)
            .deleted(UPDATED_DELETED);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDevice, device), getPersistedDevice(device));
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .type(UPDATED_TYPE)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .warranty(UPDATED_WARRANTY)
            .password(UPDATED_PASSWORD)
            .hasPattern(UPDATED_HAS_PATTERN)
            .simPinCode(UPDATED_SIM_PIN_CODE)
            .notes(UPDATED_NOTES)
            .deleted(UPDATED_DELETED);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceUpdatableFieldsEquals(partialUpdatedDevice, getPersistedDevice(partialUpdatedDevice));
    }

    @Test
    @Transactional
    void patchNonExistingDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the device
        restDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, device.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return deviceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Device getPersistedDevice(Device device) {
        return deviceRepository.findById(device.getId()).orElseThrow();
    }

    protected void assertPersistedDeviceToMatchAllProperties(Device expectedDevice) {
        assertDeviceAllPropertiesEquals(expectedDevice, getPersistedDevice(expectedDevice));
    }

    protected void assertPersistedDeviceToMatchUpdatableProperties(Device expectedDevice) {
        assertDeviceAllUpdatablePropertiesEquals(expectedDevice, getPersistedDevice(expectedDevice));
    }
}
