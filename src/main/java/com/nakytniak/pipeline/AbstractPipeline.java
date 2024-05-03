package com.nakytniak.pipeline;

import lombok.extern.slf4j.Slf4j;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.PipelineResult.State;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractPipeline<O extends PipelineOptions> {

    protected final String[] args;
    protected final O options;
    protected final Pipeline pipeline;

    protected AbstractPipeline(final String[] args) {
        this.args = args;
        options = createOptions(args);
        FileSystems.setDefaultPipelineOptions(options);
        pipeline = Pipeline.create(options);
    }

    O createOptions(final String[] args) {
        final Class<O> pipelineOptionsClass = getOptionsClass();
        PipelineOptionsFactory.register(pipelineOptionsClass);
        return PipelineOptionsFactory.fromArgs(args).withValidation().as(pipelineOptionsClass);
    }

    abstract Class<O> getOptionsClass();

    public void run() {
        log.info("Registering steps for {}.", this.getClass().getSimpleName());
        registerSteps();
        try {
            log.info("Starting to run {}.", this.getClass().getSimpleName());
            final PipelineResult pipelineResult = pipeline.run();
            final State state = pipelineResult.waitUntilFinish();
            if (state != State.DONE) {
                log.warn("{} has not completed successfully. Pipeline state: {}.",
                        this.getClass().getSimpleName(), state);
            }
        } catch (UnsupportedOperationException ex) {
            if (StringUtils.isNotBlank(((DataflowPipelineOptions) pipeline.getOptions()).getTemplateLocation())) {
                log.warn("Pipeline {} was launched in template creation mode.", this.getClass().getSimpleName());
                return;
            }
            throw new RuntimeException(ex);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    abstract void registerSteps();

}