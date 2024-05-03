package com.nakytniak.options;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.ValueProvider;

public interface MySqlDataIngestionPipelineOptions extends MySqlConfigOptions, FirestoreOptions {

    @Description("Entity name to be processed")
    @Default.String("")
    ValueProvider<String> getEntityName();

    void setEntityName(ValueProvider<String> entityName);

    @Description("Mapping file to be processed")
    @Default.String("")
    ValueProvider<String> getMappingLocation();

    void setMappingLocation(ValueProvider<String> mappingLocation);

}
