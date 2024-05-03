package com.nakytniak.options;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.ValueProvider;

public interface CsvDataIngestionPipelineOptions extends CommonOptions, FirestoreOptions {

    @Description("Csv file to be processed")
    @Default.String("")
    ValueProvider<String> getCsvFileLocation();

    void setCsvFileLocation(ValueProvider<String> csvFileLocation);

    @Description("Entity name to be processed")
    @Default.String("")
    ValueProvider<String> getEntityName();

    void setEntityName(ValueProvider<String> entityName);

}
