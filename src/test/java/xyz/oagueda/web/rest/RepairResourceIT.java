package xyz.oagueda.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.oagueda.domain.RepairAsserts.*;
import static xyz.oagueda.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import xyz.oagueda.domain.Device;
import xyz.oagueda.domain.Repair;
import xyz.oagueda.domain.enumeration.Status;
import xyz.oagueda.repository.RepairRepository;
import xyz.oagueda.service.dto.RepairDTO;
import xyz.oagueda.service.mapper.RepairMapper;

/**
 * Integration tests for the {@link RepairResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RepairResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_INTERNAL_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_OBSERVATIONS = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.TODO;
    private static final Status UPDATED_STATUS = Status.REVIEW;

    private static final Instant DEFAULT_CLOSED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BUDGET = "AAAAAAAAAA";
    private static final String UPDATED_BUDGET = "BBBBBBBBBB";

    private static final String DEFAULT_WORK_DONE = "AAAAAAAAAA";
    private static final String UPDATED_WORK_DONE = "BBBBBBBBBB";

    private static final String DEFAULT_USED_MATERIAL = "AAAAAAAAAA";
    private static final String UPDATED_USED_MATERIAL = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_MATERIAL = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_MATERIAL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IMPORTANT_DATA = false;
    private static final Boolean UPDATED_IMPORTANT_DATA = true;

    private static final String DEFAULT_TOTAL = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/repairs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepairMockMvc;

    private Repair repair;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repair createEntity(EntityManager em) {
        Repair repair = new Repair()
            .description(DEFAULT_DESCRIPTION)
            .observations(DEFAULT_OBSERVATIONS)
            .internalObservations(DEFAULT_INTERNAL_OBSERVATIONS)
            .status(DEFAULT_STATUS)
            .closedDate(DEFAULT_CLOSED_DATE)
            .budget(DEFAULT_BUDGET)
            .workDone(DEFAULT_WORK_DONE)
            .usedMaterial(DEFAULT_USED_MATERIAL)
            .customerMaterial(DEFAULT_CUSTOMER_MATERIAL)
            .importantData(DEFAULT_IMPORTANT_DATA)
            .total(DEFAULT_TOTAL);
        return repair;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repair createUpdatedEntity(EntityManager em) {
        Repair repair = new Repair()
            .description(UPDATED_DESCRIPTION)
            .observations(UPDATED_OBSERVATIONS)
            .internalObservations(UPDATED_INTERNAL_OBSERVATIONS)
            .status(UPDATED_STATUS)
            .closedDate(UPDATED_CLOSED_DATE)
            .budget(UPDATED_BUDGET)
            .workDone(UPDATED_WORK_DONE)
            .usedMaterial(UPDATED_USED_MATERIAL)
            .customerMaterial(UPDATED_CUSTOMER_MATERIAL)
            .importantData(UPDATED_IMPORTANT_DATA)
            .total(UPDATED_TOTAL);
        return repair;
    }

    @BeforeEach
    public void initTest() {
        repair = createEntity(em);
    }

    @Test
    @Transactional
    void createRepair() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);
        var returnedRepairDTO = om.readValue(
            restRepairMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repairDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RepairDTO.class
        );

        // Validate the Repair in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRepair = repairMapper.toEntity(returnedRepairDTO);
        assertRepairUpdatableFieldsEquals(returnedRepair, getPersistedRepair(returnedRepair));
    }

    @Test
    @Transactional
    void createRepairWithExistingId() throws Exception {
        // Create the Repair with an existing ID
        repair.setId(1L);
        RepairDTO repairDTO = repairMapper.toDto(repair);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepairMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repairDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        repair.setStatus(null);

        // Create the Repair, which fails.
        RepairDTO repairDTO = repairMapper.toDto(repair);

        restRepairMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repairDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRepairs() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList
        restRepairMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repair.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].internalObservations").value(hasItem(DEFAULT_INTERNAL_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)))
            .andExpect(jsonPath("$.[*].workDone").value(hasItem(DEFAULT_WORK_DONE.toString())))
            .andExpect(jsonPath("$.[*].usedMaterial").value(hasItem(DEFAULT_USED_MATERIAL.toString())))
            .andExpect(jsonPath("$.[*].customerMaterial").value(hasItem(DEFAULT_CUSTOMER_MATERIAL.toString())))
            .andExpect(jsonPath("$.[*].importantData").value(hasItem(DEFAULT_IMPORTANT_DATA.booleanValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    void getRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get the repair
        restRepairMockMvc
            .perform(get(ENTITY_API_URL_ID, repair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repair.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS.toString()))
            .andExpect(jsonPath("$.internalObservations").value(DEFAULT_INTERNAL_OBSERVATIONS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.closedDate").value(DEFAULT_CLOSED_DATE.toString()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET))
            .andExpect(jsonPath("$.workDone").value(DEFAULT_WORK_DONE.toString()))
            .andExpect(jsonPath("$.usedMaterial").value(DEFAULT_USED_MATERIAL.toString()))
            .andExpect(jsonPath("$.customerMaterial").value(DEFAULT_CUSTOMER_MATERIAL.toString()))
            .andExpect(jsonPath("$.importantData").value(DEFAULT_IMPORTANT_DATA.booleanValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL));
    }

    @Test
    @Transactional
    void getRepairsByIdFiltering() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        Long id = repair.getId();

        defaultRepairFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRepairFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRepairFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRepairsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where status equals to
        defaultRepairFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRepairsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where status in
        defaultRepairFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRepairsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where status is not null
        defaultRepairFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairsByClosedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where closedDate equals to
        defaultRepairFiltering("closedDate.equals=" + DEFAULT_CLOSED_DATE, "closedDate.equals=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllRepairsByClosedDateIsInShouldWork() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where closedDate in
        defaultRepairFiltering("closedDate.in=" + DEFAULT_CLOSED_DATE + "," + UPDATED_CLOSED_DATE, "closedDate.in=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    void getAllRepairsByClosedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where closedDate is not null
        defaultRepairFiltering("closedDate.specified=true", "closedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairsByBudgetIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where budget equals to
        defaultRepairFiltering("budget.equals=" + DEFAULT_BUDGET, "budget.equals=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllRepairsByBudgetIsInShouldWork() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where budget in
        defaultRepairFiltering("budget.in=" + DEFAULT_BUDGET + "," + UPDATED_BUDGET, "budget.in=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllRepairsByBudgetIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where budget is not null
        defaultRepairFiltering("budget.specified=true", "budget.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairsByBudgetContainsSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where budget contains
        defaultRepairFiltering("budget.contains=" + DEFAULT_BUDGET, "budget.contains=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllRepairsByBudgetNotContainsSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where budget does not contain
        defaultRepairFiltering("budget.doesNotContain=" + UPDATED_BUDGET, "budget.doesNotContain=" + DEFAULT_BUDGET);
    }

    @Test
    @Transactional
    void getAllRepairsByImportantDataIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where importantData equals to
        defaultRepairFiltering("importantData.equals=" + DEFAULT_IMPORTANT_DATA, "importantData.equals=" + UPDATED_IMPORTANT_DATA);
    }

    @Test
    @Transactional
    void getAllRepairsByImportantDataIsInShouldWork() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where importantData in
        defaultRepairFiltering(
            "importantData.in=" + DEFAULT_IMPORTANT_DATA + "," + UPDATED_IMPORTANT_DATA,
            "importantData.in=" + UPDATED_IMPORTANT_DATA
        );
    }

    @Test
    @Transactional
    void getAllRepairsByImportantDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where importantData is not null
        defaultRepairFiltering("importantData.specified=true", "importantData.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairsByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where total equals to
        defaultRepairFiltering("total.equals=" + DEFAULT_TOTAL, "total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllRepairsByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where total in
        defaultRepairFiltering("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL, "total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllRepairsByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where total is not null
        defaultRepairFiltering("total.specified=true", "total.specified=false");
    }

    @Test
    @Transactional
    void getAllRepairsByTotalContainsSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where total contains
        defaultRepairFiltering("total.contains=" + DEFAULT_TOTAL, "total.contains=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllRepairsByTotalNotContainsSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where total does not contain
        defaultRepairFiltering("total.doesNotContain=" + UPDATED_TOTAL, "total.doesNotContain=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllRepairsByDeviceIsEqualToSomething() throws Exception {
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            repairRepository.saveAndFlush(repair);
            device = DeviceResourceIT.createEntity(em);
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        em.persist(device);
        em.flush();
        repair.setDevice(device);
        repairRepository.saveAndFlush(repair);
        Long deviceId = device.getId();
        // Get all the repairList where device equals to deviceId
        defaultRepairShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the repairList where device equals to (deviceId + 1)
        defaultRepairShouldNotBeFound("deviceId.equals=" + (deviceId + 1));
    }

    private void defaultRepairFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRepairShouldBeFound(shouldBeFound);
        defaultRepairShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRepairShouldBeFound(String filter) throws Exception {
        restRepairMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repair.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].internalObservations").value(hasItem(DEFAULT_INTERNAL_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)))
            .andExpect(jsonPath("$.[*].workDone").value(hasItem(DEFAULT_WORK_DONE.toString())))
            .andExpect(jsonPath("$.[*].usedMaterial").value(hasItem(DEFAULT_USED_MATERIAL.toString())))
            .andExpect(jsonPath("$.[*].customerMaterial").value(hasItem(DEFAULT_CUSTOMER_MATERIAL.toString())))
            .andExpect(jsonPath("$.[*].importantData").value(hasItem(DEFAULT_IMPORTANT_DATA.booleanValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));

        // Check, that the count call also returns 1
        restRepairMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRepairShouldNotBeFound(String filter) throws Exception {
        restRepairMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRepairMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRepair() throws Exception {
        // Get the repair
        restRepairMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repair
        Repair updatedRepair = repairRepository.findById(repair.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRepair are not directly saved in db
        em.detach(updatedRepair);
        updatedRepair
            .description(UPDATED_DESCRIPTION)
            .observations(UPDATED_OBSERVATIONS)
            .internalObservations(UPDATED_INTERNAL_OBSERVATIONS)
            .status(UPDATED_STATUS)
            .closedDate(UPDATED_CLOSED_DATE)
            .budget(UPDATED_BUDGET)
            .workDone(UPDATED_WORK_DONE)
            .usedMaterial(UPDATED_USED_MATERIAL)
            .customerMaterial(UPDATED_CUSTOMER_MATERIAL)
            .importantData(UPDATED_IMPORTANT_DATA)
            .total(UPDATED_TOTAL);
        RepairDTO repairDTO = repairMapper.toDto(updatedRepair);

        restRepairMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repairDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repairDTO))
            )
            .andExpect(status().isOk());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRepairToMatchAllProperties(updatedRepair);
    }

    @Test
    @Transactional
    void putNonExistingRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repairDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repairDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(repairDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repairDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepairWithPatch() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repair using partial update
        Repair partialUpdatedRepair = new Repair();
        partialUpdatedRepair.setId(repair.getId());

        partialUpdatedRepair
            .observations(UPDATED_OBSERVATIONS)
            .closedDate(UPDATED_CLOSED_DATE)
            .workDone(UPDATED_WORK_DONE)
            .customerMaterial(UPDATED_CUSTOMER_MATERIAL);

        restRepairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepair.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRepair))
            )
            .andExpect(status().isOk());

        // Validate the Repair in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRepairUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRepair, repair), getPersistedRepair(repair));
    }

    @Test
    @Transactional
    void fullUpdateRepairWithPatch() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repair using partial update
        Repair partialUpdatedRepair = new Repair();
        partialUpdatedRepair.setId(repair.getId());

        partialUpdatedRepair
            .description(UPDATED_DESCRIPTION)
            .observations(UPDATED_OBSERVATIONS)
            .internalObservations(UPDATED_INTERNAL_OBSERVATIONS)
            .status(UPDATED_STATUS)
            .closedDate(UPDATED_CLOSED_DATE)
            .budget(UPDATED_BUDGET)
            .workDone(UPDATED_WORK_DONE)
            .usedMaterial(UPDATED_USED_MATERIAL)
            .customerMaterial(UPDATED_CUSTOMER_MATERIAL)
            .importantData(UPDATED_IMPORTANT_DATA)
            .total(UPDATED_TOTAL);

        restRepairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepair.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRepair))
            )
            .andExpect(status().isOk());

        // Validate the Repair in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRepairUpdatableFieldsEquals(partialUpdatedRepair, getPersistedRepair(partialUpdatedRepair));
    }

    @Test
    @Transactional
    void patchNonExistingRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repairDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(repairDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(repairDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(repairDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the repair
        restRepairMockMvc
            .perform(delete(ENTITY_API_URL_ID, repair.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return repairRepository.count();
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

    protected Repair getPersistedRepair(Repair repair) {
        return repairRepository.findById(repair.getId()).orElseThrow();
    }

    protected void assertPersistedRepairToMatchAllProperties(Repair expectedRepair) {
        assertRepairAllPropertiesEquals(expectedRepair, getPersistedRepair(expectedRepair));
    }

    protected void assertPersistedRepairToMatchUpdatableProperties(Repair expectedRepair) {
        assertRepairAllUpdatablePropertiesEquals(expectedRepair, getPersistedRepair(expectedRepair));
    }
}
