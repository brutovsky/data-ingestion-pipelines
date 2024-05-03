package com.nakytniak.helper;

import com.nakytniak.options.CommonOptions;
import com.nakytniak.pipeline.AbstractPipeline;
import com.nakytniak.pipeline.CsvToFirestorePipeline;
import com.nakytniak.pipeline.MySqlToFirestorePipeline;
import com.nakytniak.pipeline.PipelineType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PipelineFactory {

    private static final Map<PipelineType, Function<String[], AbstractPipeline<?>>>
            PIPELINE_TYPE_TO_CONSTRUCTOR_MAPPING;

    static {
        PIPELINE_TYPE_TO_CONSTRUCTOR_MAPPING = new EnumMap<>(PipelineType.class);
        PIPELINE_TYPE_TO_CONSTRUCTOR_MAPPING.put(PipelineType.CSV_TO_FIRESTORE, CsvToFirestorePipeline::new);
        PIPELINE_TYPE_TO_CONSTRUCTOR_MAPPING.put(PipelineType.MYSQL_TO_FIRESTORE, MySqlToFirestorePipeline::new);
    }

    public static AbstractPipeline<?> createPipeline(final String[] args) {
        final PipelineType pipelineType = resolvePipelineType(args);
        final AbstractPipeline<?> pipeline = PIPELINE_TYPE_TO_CONSTRUCTOR_MAPPING.get(pipelineType).apply(args);
        if (Objects.isNull(pipeline)) {
            throw new IllegalArgumentException(String.format("Pipeline type [%s] is not supported.", pipelineType));
        }
        return pipeline;
    }

    private static PipelineType resolvePipelineType(final String[] args) {
        PipelineOptionsFactory.register(CommonOptions.class);
        final CommonOptions options = PipelineOptionsFactory.fromArgs(args).withoutStrictParsing()
                .as(CommonOptions.class);
        return options.getPipelineType().get();
    }

}