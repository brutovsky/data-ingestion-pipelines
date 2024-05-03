package com.nakytniak.options;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.ValueProvider;

public interface FirestoreOptions extends DataflowPipelineOptions {
    @Description("Firestore collection name")
    @Default.String("")
    ValueProvider<String> getFirestoreCollectionName();

    void setFirestoreCollectionName(ValueProvider<String> firestoreCollectionName);

    @Description("Database id")
    @Default.String("")
    ValueProvider<String> getDatabaseId();

    void setDatabaseId(ValueProvider<String> databaseId);
}
