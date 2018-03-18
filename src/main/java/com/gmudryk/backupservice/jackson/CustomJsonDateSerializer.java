package com.gmudryk.backupservice.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gmudryk.backupservice.constants.Constants;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomJsonDateSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {

        String formattedDate = dateFormat.format(date);
        gen.writeString(formattedDate);

    }

}
