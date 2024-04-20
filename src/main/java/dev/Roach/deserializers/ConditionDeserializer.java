package dev.Roach.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionDeserializer extends JsonDeserializer<Map<String, Object>> {
    @Override
    public Map<String, Object> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Map<String, Object> conditionMap = new HashMap<>();
        jp.nextToken();

        while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
            String field = jp.currentName();
            jp.nextToken();

            if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
                List<String> values = jp.readValueAs(new TypeReference<List<String>>() {});
                conditionMap.put(field, values);
            } else {
                String value = jp.getText();
                conditionMap.put(field, value);
            }
            jp.nextToken();
        }
        return conditionMap;
    }
}
