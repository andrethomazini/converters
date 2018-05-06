package com.converter.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.converter.domain.Converter;
import com.converter.service.ConverterService;
import com.converter.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Converter.
 */
@RestController
@RequestMapping("/api")
public class ConverterResource {

    private final Logger log = LoggerFactory.getLogger(ConverterResource.class);

    private static final String ENTITY_NAME = "converter";

    private final ConverterService converterService;

    public ConverterResource(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * POST  /converters : Create a new converter.
     *
     * @param converter the converter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new converter, or with status 400 (Bad Request) if the converter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/converters")
    @Timed
    public ResponseEntity<Converter> createConverter(@Valid @RequestBody Converter converter) throws URISyntaxException {
        log.debug("REST request to save Converter : {}", converter);
        if (converter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new converter cannot already have an ID")).body(null);
        }
        Converter result = converterService.save(converter);
        return ResponseEntity.created(new URI("/api/converters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /converters : Updates an existing converter.
     *
     * @param converter the converter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated converter,
     * or with status 400 (Bad Request) if the converter is not valid,
     * or with status 500 (Internal Server Error) if the converter couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/converters")
    @Timed
    public ResponseEntity<Converter> updateConverter(@Valid @RequestBody Converter converter) throws URISyntaxException {
        log.debug("REST request to update Converter : {}", converter);
        if (converter.getId() == null) {
            return createConverter(converter);
        }
        Converter result = converterService.save(converter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, converter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /converters : get all the converters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of converters in body
     */
    @GetMapping("/converters")
    @Timed
    public List<Converter> getAllConverters() {
        log.debug("REST request to get all Converters");
        return converterService.findAll();
    }

    /**
     * GET  /converters/:id : get the "id" converter.
     *
     * @param id the id of the converter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the converter, or with status 404 (Not Found)
     */
    @GetMapping("/converters/{id}")
    @Timed
    public ResponseEntity<Converter> getConverter(@PathVariable Long id) {
        log.debug("REST request to get Converter : {}", id);
        Converter converter = converterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(converter));
    }

    /**
     * DELETE  /converters/:id : delete the "id" converter.
     *
     * @param id the id of the converter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/converters/{id}")
    @Timed
    public ResponseEntity<Void> deleteConverter(@PathVariable Long id) {
        log.debug("REST request to delete Converter : {}", id);
        converterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/converters?query=:query : search for the converter corresponding
     * to the query.
     *
     * @param query the query of the converter search
     * @return the result of the search
     */
    @GetMapping("/_search/converters")
    @Timed
    public List<Converter> searchConverters(@RequestParam String query) {
        log.debug("REST request to search Converters for query {}", query);
        return converterService.search(query);
    }

    @PostMapping("/converter/tika")
    @Timed
    public void convertWithTika(@Valid @RequestBody MultipartFile file) throws URISyntaxException {
        log.debug("Convertendo com Apache Tika");




        //return ResponseEntity.created(new URI("/api/converters/" + result.getId()))
          //  .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            //.body(result);
    }

}
