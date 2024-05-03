package com.nakytniak.function;

import com.nakytniak.model.BaseEntityModel;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import java.io.Serializable;

public class MakeBatchers extends DoFn<BaseEntityModel, KV<String, BaseEntityModel>> implements Serializable {
    @ProcessElement
    public void processElement(ProcessContext c) {
        BaseEntityModel entity = c.element();
        c.output(KV.of("key", entity));
    }
}
