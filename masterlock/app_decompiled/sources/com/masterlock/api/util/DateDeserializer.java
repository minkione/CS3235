package com.masterlock.api.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import java.util.Date;
import org.joda.time.format.ISODateTimeFormat;

public class DateDeserializer implements JsonDeserializer<Date> {
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        try {
            return ISODateTimeFormat.dateTimeParser().parseDateTime(jsonElement.getAsJsonPrimitive().getAsString()).toDate();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
