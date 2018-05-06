package com.converter.service.impl;

import com.converter.service.ConverterService;
import com.converter.domain.Converter;
import com.converter.repository.ConverterRepository;
import com.converter.repository.search.ConverterSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Converter.
 */
@Service
@Transactional
public class ConverterServiceImpl implements ConverterService{

    private final Logger log = LoggerFactory.getLogger(ConverterServiceImpl.class);

    private final ConverterRepository converterRepository;

    private final ConverterSearchRepository converterSearchRepository;

    public ConverterServiceImpl(ConverterRepository converterRepository, ConverterSearchRepository converterSearchRepository) {
        this.converterRepository = converterRepository;
        this.converterSearchRepository = converterSearchRepository;
    }

    /**
     * Save a converter.
     *
     * @param converter the entity to save
     * @return the persisted entity
     */
    @Override
    public Converter save(Converter converter) {
        log.debug("Request to save Converter : {}", converter);
        Converter result = converterRepository.save(converter);
        converterSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the converters.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Converter> findAll() {
        log.debug("Request to get all Converters");
        return converterRepository.findAll();
    }

    /**
     *  Get one converter by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Converter findOne(Long id) {
        log.debug("Request to get Converter : {}", id);
        return converterRepository.findOne(id);
    }

    /**
     *  Delete the  converter by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Converter : {}", id);
        converterRepository.delete(id);
        converterSearchRepository.delete(id);
    }

    /**
     * Search for the converter corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Converter> search(String query) {
        log.debug("Request to search Converters for query {}", query);
        return StreamSupport
            .stream(converterSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
