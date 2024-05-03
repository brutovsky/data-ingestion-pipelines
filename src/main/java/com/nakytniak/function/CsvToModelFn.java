package com.nakytniak.function;

import com.nakytniak.helper.SerializableSupplier;
import com.nakytniak.mapper.CsvRowMapper;
import com.nakytniak.model.BaseEntityModel;
import org.apache.beam.sdk.transforms.DoFn;

public class CsvToModelFn<T extends BaseEntityModel> extends DoFn<String, T> {
    final SerializableSupplier<CsvRowMapper<? extends T>> rowMapperSupplier;

    public CsvToModelFn(final SerializableSupplier<CsvRowMapper<? extends T>> rowMapperSupplier) {
        this.rowMapperSupplier = rowMapperSupplier;
    }

    @ProcessElement
    public void processElement(ProcessContext c) throws Exception {
        final CsvRowMapper<? extends T> mapper = rowMapperSupplier.get();
        String line = c.element();
        T model = mapper.mapRow(line);
        c.output(model);
    }
}