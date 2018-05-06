package com.converter.service;

import com.converter.domain.Converter;
import java.util.List;

/**
 * Service Interface for managing Converter.
 */
public interface ConverterService {

    /**
     * Save a converter.
     *
     * @param converter the entity to save
     * @return the persisted entity
     */
    Converter save(Converter converter);

    /**
     *  Get all the converters.
     *
     *  @return the list of entities
     */
    List<Converter> findAll();

    /**
     *  Get the "id" converter.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Converter findOne(Long id);

    /**
     *  Delete the "id" converter.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the converter corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<Converter> search(String query);
}
