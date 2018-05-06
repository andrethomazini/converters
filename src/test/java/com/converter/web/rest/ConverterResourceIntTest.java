package com.converter.web.rest;

import com.converter.ConverterApp;

import com.converter.domain.Converter;
import com.converter.repository.ConverterRepository;
import com.converter.service.ConverterService;
import com.converter.repository.search.ConverterSearchRepository;
import com.converter.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ConverterResource REST controller.
 *
 * @see ConverterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConverterApp.class)
public class ConverterResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private ConverterRepository converterRepository;

    @Autowired
    private ConverterService converterService;

    @Autowired
    private ConverterSearchRepository converterSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConverterMockMvc;

    private Converter converter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConverterResource converterResource = new ConverterResource(converterService);
        this.restConverterMockMvc = MockMvcBuilders.standaloneSetup(converterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Converter createEntity(EntityManager em) {
        Converter converter = new Converter()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE);
        return converter;
    }

    @Before
    public void initTest() {
        converterSearchRepository.deleteAll();
        converter = createEntity(em);
    }

    @Test
    @Transactional
    public void createConverter() throws Exception {
        int databaseSizeBeforeCreate = converterRepository.findAll().size();

        // Create the Converter
        restConverterMockMvc.perform(post("/api/converters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(converter)))
            .andExpect(status().isCreated());

        // Validate the Converter in the database
        List<Converter> converterList = converterRepository.findAll();
        assertThat(converterList).hasSize(databaseSizeBeforeCreate + 1);
        Converter testConverter = converterList.get(converterList.size() - 1);
        assertThat(testConverter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConverter.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Converter in Elasticsearch
        Converter converterEs = converterSearchRepository.findOne(testConverter.getId());
        assertThat(converterEs).isEqualToComparingFieldByField(testConverter);
    }

    @Test
    @Transactional
    public void createConverterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = converterRepository.findAll().size();

        // Create the Converter with an existing ID
        converter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConverterMockMvc.perform(post("/api/converters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(converter)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Converter> converterList = converterRepository.findAll();
        assertThat(converterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = converterRepository.findAll().size();
        // set the field null
        converter.setName(null);

        // Create the Converter, which fails.

        restConverterMockMvc.perform(post("/api/converters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(converter)))
            .andExpect(status().isBadRequest());

        List<Converter> converterList = converterRepository.findAll();
        assertThat(converterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConverters() throws Exception {
        // Initialize the database
        converterRepository.saveAndFlush(converter);

        // Get all the converterList
        restConverterMockMvc.perform(get("/api/converters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(converter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getConverter() throws Exception {
        // Initialize the database
        converterRepository.saveAndFlush(converter);

        // Get the converter
        restConverterMockMvc.perform(get("/api/converters/{id}", converter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(converter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConverter() throws Exception {
        // Get the converter
        restConverterMockMvc.perform(get("/api/converters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConverter() throws Exception {
        // Initialize the database
        converterService.save(converter);

        int databaseSizeBeforeUpdate = converterRepository.findAll().size();

        // Update the converter
        Converter updatedConverter = converterRepository.findOne(converter.getId());
        updatedConverter
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);

        restConverterMockMvc.perform(put("/api/converters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConverter)))
            .andExpect(status().isOk());

        // Validate the Converter in the database
        List<Converter> converterList = converterRepository.findAll();
        assertThat(converterList).hasSize(databaseSizeBeforeUpdate);
        Converter testConverter = converterList.get(converterList.size() - 1);
        assertThat(testConverter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConverter.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Converter in Elasticsearch
        Converter converterEs = converterSearchRepository.findOne(testConverter.getId());
        assertThat(converterEs).isEqualToComparingFieldByField(testConverter);
    }

    @Test
    @Transactional
    public void updateNonExistingConverter() throws Exception {
        int databaseSizeBeforeUpdate = converterRepository.findAll().size();

        // Create the Converter

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConverterMockMvc.perform(put("/api/converters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(converter)))
            .andExpect(status().isCreated());

        // Validate the Converter in the database
        List<Converter> converterList = converterRepository.findAll();
        assertThat(converterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConverter() throws Exception {
        // Initialize the database
        converterService.save(converter);

        int databaseSizeBeforeDelete = converterRepository.findAll().size();

        // Get the converter
        restConverterMockMvc.perform(delete("/api/converters/{id}", converter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean converterExistsInEs = converterSearchRepository.exists(converter.getId());
        assertThat(converterExistsInEs).isFalse();

        // Validate the database is empty
        List<Converter> converterList = converterRepository.findAll();
        assertThat(converterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConverter() throws Exception {
        // Initialize the database
        converterService.save(converter);

        // Search the converter
        restConverterMockMvc.perform(get("/api/_search/converters?query=id:" + converter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(converter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Converter.class);
        Converter converter1 = new Converter();
        converter1.setId(1L);
        Converter converter2 = new Converter();
        converter2.setId(converter1.getId());
        assertThat(converter1).isEqualTo(converter2);
        converter2.setId(2L);
        assertThat(converter1).isNotEqualTo(converter2);
        converter1.setId(null);
        assertThat(converter1).isNotEqualTo(converter2);
    }
}
