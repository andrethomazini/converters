package com.converter.repository.search;

import com.converter.domain.Converter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Converter entity.
 */
public interface ConverterSearchRepository extends ElasticsearchRepository<Converter, Long> {
}
