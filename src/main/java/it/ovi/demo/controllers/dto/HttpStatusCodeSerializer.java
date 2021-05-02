package it.ovi.demo.controllers.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * Serializes a {@link HttpStatus} as its code.
 */
public class HttpStatusCodeSerializer extends StdSerializer<HttpStatus> {
    public HttpStatusCodeSerializer() {
        super(HttpStatus.class);
    }

    @Override
    public void serialize(HttpStatus httpStatus, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (httpStatus != null) {
            jsonGenerator.writeNumber(httpStatus.value());
        } else {
            jsonGenerator.writeNull();
        }
    }
}
