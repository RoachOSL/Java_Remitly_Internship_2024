package dev.Roach.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.Roach.datamodel.JSONRolePolicy;

import java.io.IOException;

public class JSONRolePolicyMapper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JSONRolePolicy deserializeJSON(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, JSONRolePolicy.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON to JSONRolePolicy object", e);
        }
    }

}
