package com.nakytniak.function;

import com.nakytniak.helper.SerializableSupplier;
import com.nakytniak.mapper.CsvHeaderMapper;
import com.nakytniak.model.BaseEntityModel;
import org.apache.beam.sdk.transforms.DoFn;

public class FilterCSVHeaderFn<T extends BaseEntityModel> extends DoFn<String, String> {

    final SerializableSupplier<CsvHeaderMapper<? extends T>> csvHeaderSupplier;

    public FilterCSVHeaderFn(final SerializableSupplier<CsvHeaderMapper<? extends T>> csvHeaderSupplier) {
        this.csvHeaderSupplier = csvHeaderSupplier;
    }

    @ProcessElement
    public void processElement(ProcessContext c) throws Exception {
        String row = c.element();
        String header = csvHeaderSupplier.get().getHeader(row);
        if (!row.equals(header)) {
            c.output(row);
        }
    }
}