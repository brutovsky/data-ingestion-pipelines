package com.nakytniak.options;

import com.nakytniak.pipeline.PipelineType;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.ValueProvider;

public interface CommonOptions extends DataflowPipelineOptions {

    @Description("Determines the specific pipeline that will be executed")
    ValueProvider<PipelineType> getPipelineType();

    void setPipelineType(ValueProvider<PipelineType> pipelineType);

}