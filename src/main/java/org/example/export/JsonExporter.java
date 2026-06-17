package org.example.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonExporter {

    private final ObjectMapper mapper;

    public JsonExporter() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void export(List<?> records, String outputPath) throws IOException {
        new File("output").mkdirs();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputPath), records);
    }
}
