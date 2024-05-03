package com.nakytniak;

import com.nakytniak.helper.PipelineFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PipelineStarter {

    public static void main(final String[] args) {
        PipelineFactory.createPipeline(args).run();
    }

}

