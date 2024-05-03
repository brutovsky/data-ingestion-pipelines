package com.nakytniak.pipeline;

import com.nakytniak.dao.sql.DataSourceProvider;
import com.nakytniak.dao.sql.DataSourceVendor;
import com.nakytniak.function.FetchSqlDataFunction;
import com.nakytniak.function.JsonToMappingFn;
import com.nakytniak.function.MakeBatchers;
import com.nakytniak.function.WholeFileReader;
import com.nakytniak.function.WriteToFirestoreFn;
import com.nakytniak.helper.SerializableSupplier;
import com.nakytniak.mapper.SqlEntityRowMapper;
import com.nakytniak.model.BaseEntityModel;
import com.nakytniak.model.ModelContainer;
import com.nakytniak.model.mapping.Mapping;
import com.nakytniak.options.MySqlDataIngestionPipelineOptions;
import com.nakytniak.utils.MySqlModelUtil;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.options.ValueProvider.StaticValueProvider;
import org.apache.beam.sdk.transforms.GroupIntoBatches;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlToFirestorePipeline extends AbstractPipeline<MySqlDataIngestionPipelineOptions> {

    private static final Logger log = LoggerFactory.getLogger(MySqlToFirestorePipeline.class);

    private static final int BATCH_SIZE = 100;

    public MySqlToFirestorePipeline(final String[] args) {
        super(args);
    }

    @Override
    public void registerSteps() {
        log.info("Starting pipeline MySqlToFirestorePipeline");
        final DataSourceProvider mySqlDataSourceProvider = DataSourceProvider.builder()
                .dbConnection(options.getMySqlConnectionUrl())
                .username(options.getMySqlUsername())
                .password(options.getMySqlPassword())
                .dataSourceVendor(DataSourceVendor.MYSQL)
                .build();
        final ModelContainer modelContainer = new ModelContainer();
        final SerializableSupplier<SqlEntityRowMapper<? extends BaseEntityModel>> rowMapperSupplier =
                MySqlModelUtil.createSqlEntityRowMapperSupplier(options.getEntityName(), modelContainer);

        // Read JSON data from a file
        final PCollection<String> jsonLines = pipeline
                .apply("Read JSON File", FileIO.match().filepattern(options.getMappingLocation()))
                .apply("Read Whole File", FileIO.readMatches())
                .apply("Convert to String", ParDo.of(new WholeFileReader()));

        // Parse and deserialize JSON data into Mapping objects
        final PCollection<Mapping> mapping = jsonLines.apply(ParDo.of(new JsonToMappingFn()));

        final PCollection<? extends BaseEntityModel> entities = mapping
                .apply("Fetching data from MySql",
                        ParDo.of(new FetchSqlDataFunction<>(mySqlDataSourceProvider, rowMapperSupplier)));

        // Transform data and write to Firestore in batches
        entities.apply("Map to KV", ParDo.of(new MakeBatchers()))
                .apply("Group into Batches", GroupIntoBatches.ofSize(BATCH_SIZE))
                .apply("Write Batches to Firestore",
                        ParDo.of(new WriteToFirestoreFn(StaticValueProvider.of(options.getProject()),
                                options.getDatabaseId(),
                                options.getFirestoreCollectionName())));

    }

    @Override
    Class<MySqlDataIngestionPipelineOptions> getOptionsClass() {
        return MySqlDataIngestionPipelineOptions.class;
    }

}
