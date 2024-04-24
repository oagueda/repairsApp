package xyz.oagueda.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.oagueda.domain.PatternAsserts.*;
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
import xyz.oagueda.domain.Pattern;
import xyz.oagueda.repository.PatternRepository;
import xyz.oagueda.service.dto.PatternDTO;
import xyz.oagueda.service.mapper.PatternMapper;

/**
 * Integration tests for the {@link PatternResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatternResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patterns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private PatternMapper patternMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatternMockMvc;

    private Pattern pattern;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pattern createEntity(EntityManager em) {
        Pattern pattern = new Pattern().code(DEFAULT_CODE);
        return pattern;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pattern createUpdatedEntity(EntityManager em) {
        Pattern pattern = new Pattern().code(UPDATED_CODE);
        return pattern;
    }

    @BeforeEach
    public void initTest() {
        pattern = createEntity(em);
    }

    @Test
    @Transactional
    void createPattern() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pattern
        PatternDTO patternDTO = patternMapper.toDto(pattern);
        var returnedPatternDTO = om.readValue(
            restPatternMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patternDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PatternDTO.class
        );

        // Validate the Pattern in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPattern = patternMapper.toEntity(returnedPatternDTO);
        assertPatternUpdatableFieldsEquals(returnedPattern, getPersistedPattern(returnedPattern));
    }

    @Test
    @Transactional
    void createPatternWithExistingId() throws Exception {
        // Create the Pattern with an existing ID
        pattern.setId(1L);
        PatternDTO patternDTO = patternMapper.toDto(pattern);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patternDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pattern in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatterns() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get all the patternList
        restPatternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getPattern() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get the pattern
        restPatternMockMvc
            .perform(get(ENTITY_API_URL_ID, pattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pattern.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getPatternsByIdFiltering() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        Long id = pattern.getId();

        defaultPatternFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPatternFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPatternFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPatternsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get all the patternList where code equals to
        defaultPatternFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPatternsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get all the patternList where code in
        defaultPatternFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPatternsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get all the patternList where code is not null
        defaultPatternFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllPatternsByCodeContainsSomething() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get all the patternList where code contains
        defaultPatternFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPatternsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get all the patternList where code does not contain
        defaultPatternFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    private void defaultPatternFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPatternShouldBeFound(shouldBeFound);
        defaultPatternShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatternShouldBeFound(String filter) throws Exception {
        restPatternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restPatternMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatternShouldNotBeFound(String filter) throws Exception {
        restPatternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatternMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPattern() throws Exception {
        // Get the pattern
        restPatternMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPattern() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pattern
        Pattern updatedPattern = patternRepository.findById(pattern.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPattern are not directly saved in db
        em.detach(updatedPattern);
        updatedPattern.code(UPDATED_CODE);
        PatternDTO patternDTO = patternMapper.toDto(updatedPattern);

        restPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patternDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patternDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPatternToMatchAllProperties(updatedPattern);
    }

    @Test
    @Transactional
    void putNonExistingPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pattern.setId(longCount.incrementAndGet());

        // Create the Pattern
        PatternDTO patternDTO = patternMapper.toDto(pattern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patternDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patternDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pattern.setId(longCount.incrementAndGet());

        // Create the Pattern
        PatternDTO patternDTO = patternMapper.toDto(pattern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patternDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pattern.setId(longCount.incrementAndGet());

        // Create the Pattern
        PatternDTO patternDTO = patternMapper.toDto(pattern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatternMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patternDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatternWithPatch() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pattern using partial update
        Pattern partialUpdatedPattern = new Pattern();
        partialUpdatedPattern.setId(pattern.getId());

        partialUpdatedPattern.code(UPDATED_CODE);

        restPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPattern.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPattern))
            )
            .andExpect(status().isOk());

        // Validate the Pattern in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatternUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPattern, pattern), getPersistedPattern(pattern));
    }

    @Test
    @Transactional
    void fullUpdatePatternWithPatch() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pattern using partial update
        Pattern partialUpdatedPattern = new Pattern();
        partialUpdatedPattern.setId(pattern.getId());

        partialUpdatedPattern.code(UPDATED_CODE);

        restPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPattern.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPattern))
            )
            .andExpect(status().isOk());

        // Validate the Pattern in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatternUpdatableFieldsEquals(partialUpdatedPattern, getPersistedPattern(partialUpdatedPattern));
    }

    @Test
    @Transactional
    void patchNonExistingPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pattern.setId(longCount.incrementAndGet());

        // Create the Pattern
        PatternDTO patternDTO = patternMapper.toDto(pattern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patternDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patternDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pattern.setId(longCount.incrementAndGet());

        // Create the Pattern
        PatternDTO patternDTO = patternMapper.toDto(pattern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patternDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pattern.setId(longCount.incrementAndGet());

        // Create the Pattern
        PatternDTO patternDTO = patternMapper.toDto(pattern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatternMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(patternDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePattern() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pattern
        restPatternMockMvc
            .perform(delete(ENTITY_API_URL_ID, pattern.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return patternRepository.count();
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

    protected Pattern getPersistedPattern(Pattern pattern) {
        return patternRepository.findById(pattern.getId()).orElseThrow();
    }

    protected void assertPersistedPatternToMatchAllProperties(Pattern expectedPattern) {
        assertPatternAllPropertiesEquals(expectedPattern, getPersistedPattern(expectedPattern));
    }

    protected void assertPersistedPatternToMatchUpdatableProperties(Pattern expectedPattern) {
        assertPatternAllUpdatablePropertiesEquals(expectedPattern, getPersistedPattern(expectedPattern));
    }
}
