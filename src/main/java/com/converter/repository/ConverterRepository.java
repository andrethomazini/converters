package com.converter.repository;

import com.converter.domain.Converter;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Converter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConverterRepository extends JpaRepository<Converter,Long> {

}
