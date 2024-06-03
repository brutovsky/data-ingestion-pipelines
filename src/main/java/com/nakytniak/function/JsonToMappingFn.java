package com.nakytniak.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nakytniak.dao.sql.DataSourceProvider;
import com.nakytniak.model.mapping.Mapping;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonToMappingFn extends DoFn<String, Mapping> {

    private static final Logger log = LoggerFactory.getLogger(JsonToMappingFn.class);

    private static final long serialVersionUID = -7673623978849313108L;

    @ProcessElement
    public void processElement(final ProcessContext c) throws JsonProcessingException {
        try {
            final String jsonString = c.element();
            final ObjectMapper mapper = new ObjectMapper();
            final Mapping mapping = mapper.readValue(jsonString, Mapping.class);
            log.info("Mapping is ready: " + mapping);
            c.output(mapping);
        } catch (Exception e) {
            System.err.println("Error processing JSON: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
