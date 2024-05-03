package com.nakytniak.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nakytniak.model.mapping.Mapping;
import org.apache.beam.sdk.transforms.DoFn;

public class JsonToMappingFn extends DoFn<String, Mapping> {

    private static final long serialVersionUID = -7673623978849313108L;

    @ProcessElement
    public void processElement(final ProcessContext c) {
        try {
            final String jsonString = c.element();
            final ObjectMapper mapper = new ObjectMapper();
            final Mapping mapping = mapper.readValue(jsonString, Mapping.class);
            c.output(mapping);
        } catch (Exception e) {
            System.err.println("Error processing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
