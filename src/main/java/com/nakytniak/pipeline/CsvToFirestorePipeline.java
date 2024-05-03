package com.nakytniak.pipeline;

import com.nakytniak.function.CsvToModelFn;
import com.nakytniak.function.FilterCSVHeaderFn;
import com.nakytniak.function.MakeBatchers;
import com.nakytniak.function.WriteToFirestoreFn;
import com.nakytniak.helper.SerializableSupplier;
import com.nakytniak.mapper.CsvHeaderMapper;
import com.nakytniak.mapper.CsvRowMapper;
import com.nakytniak.model.BaseEntityModel;
import com.nakytniak.model.ModelContainer;
import com.nakytniak.options.CsvDataIngestionPipelineOptions;
import com.nakytniak.utils.MySqlModelUtil;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.ValueProvider.StaticValueProvider;
import org.apache.beam.sdk.transforms.GroupIntoBatches;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvToFirestorePipeline extends AbstractPipeline<CsvDataIngestionPipelineOptions> {

    private static final Logger log = LoggerFactory.getLogger(CsvToFirestorePipeline.class);

    private static final int BATCH_SIZE = 100;

    public CsvToFirestorePipeline(final String[] args) {
        super(args);
    }

    @Override
    public void registerSteps() {
        log.info("Starting pipeline CsvToFirestorePipeline");

        final ModelContainer modelContainer = new ModelContainer();
        final SerializableSupplier<CsvRowMapper<? extends BaseEntityModel>> rowMapperSupplier =
                MySqlModelUtil.createCsvEntityRowMapperSupplier(options.getEntityName(), modelContainer);
        final SerializableSupplier<CsvHeaderMapper<? extends BaseEntityModel>> csvHeaderSupplier =
                MySqlModelUtil.createCsvHeaderSupplier(options.getEntityName(), modelContainer);

        // Read CSV data from a file
        final PCollection<String> csvLines = pipeline
                .apply("Read CSV Files", TextIO.read().from(options.getCsvFileLocation()))
                .apply("Skip Header", ParDo.of(new FilterCSVHeaderFn<>(csvHeaderSupplier)));

        // Parse and deserialize CSV data into StudentsInfo objects
        final PCollection<BaseEntityModel> entities = csvLines.apply("Parse CSV Lines",
                ParDo.of(new CsvToModelFn<>(rowMapperSupplier)));

        // Transform data and write to Firestore in batches
        entities.apply("Map to KV", ParDo.of(new MakeBatchers()))
                .apply("Group into Batches", GroupIntoBatches.<String, BaseEntityModel>ofSize(BATCH_SIZE))
                .apply("Write Batches to Firestore", ParDo.of(new WriteToFirestoreFn(
                        StaticValueProvider.of(options.getProject()), options.getDatabaseId(),
                        options.getFirestoreCollectionName())));
    }

    @Override
    public Class<CsvDataIngestionPipelineOptions> getOptionsClass() {
        return CsvDataIngestionPipelineOptions.class;
    }

}