package dev.Roach.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralListOrStringOrMapDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonToken currentToken = jp.getCurrentToken();

        if (currentToken.equals(JsonToken.START_ARRAY)) {
            List<String> values = jp.readValueAs(new TypeReference<List<String>>() {
            });
            return values != null ? values : Collections.emptyList();
        } else if (currentToken.equals(JsonToken.START_OBJECT)) {
            Map<String, Object> map = new HashMap<>();
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jp.currentName();
                jp.nextToken();
                if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
                    List<String> listValues = jp.readValueAs(new TypeReference<List<String>>() {
                    });
                    map.put(fieldName, listValues != null ? listValues : Collections.emptyList());
                } else if (jp.getCurrentToken().isScalarValue()) {
                    map.put(fieldName, jp.getText());
                } else {
                    map.put(fieldName, deserialize(jp, ctxt));
                }
            }
            return map;
        } else if (currentToken.isScalarValue()) {
            String text = jp.getText();
            return text != null ? text : "";
        } else {
            return "";
        }
    }
}
