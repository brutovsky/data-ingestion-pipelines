package com.nakytniak.function;

import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.FileIO.ReadableFile;
import org.apache.beam.sdk.transforms.DoFn;

import java.io.IOException;

public class WholeFileReader extends DoFn<ReadableFile, String> {

    private static final long serialVersionUID = -1627253743354552866L;

    @ProcessElement
    public void processElement(ProcessContext c) throws IOException {
        FileIO.ReadableFile file = c.element();
        byte[] fileBytes = file.readFullyAsBytes();
        String fileContent = new String(fileBytes);
        c.output(fileContent);
    }
}