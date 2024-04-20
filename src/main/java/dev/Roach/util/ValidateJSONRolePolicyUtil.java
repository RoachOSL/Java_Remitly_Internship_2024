package dev.Roach.util;

import dev.Roach.datamodel.JSONRolePolicy;
import dev.Roach.datamodel.Statement;
import dev.Roach.exceptions.JSONValidationException;
import dev.Roach.mapper.JSONRolePolicyMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ValidateJSONRolePolicyUtil {

    public boolean validateJSONFromStringAndFile(String input) throws IOException, JSONValidationException {
        if (isFilePath(input)) {
            return validateJSONFromFile(input);
        } else {
            return validateJSONFromString(input);
        }
    }

    private boolean validateResource(JSONRolePolicy jsonRolePolicy) throws JSONValidationException {
        if (jsonRolePolicy == null || jsonRolePolicy.getPolicyDocument() == null) {
            throw new JSONValidationException("Invalid JSON: Policy document is missing.");
        }
        List<Statement> statementList = jsonRolePolicy.getPolicyDocument().getStatement();
        if (statementList == null || statementList.isEmpty()) {
            throw new JSONValidationException("Invalid JSON: No statements found in the policy document.");
        }
        for (Statement statement : statementList) {
            Object resource = statement.getResource();
            if (resource instanceof String && !((String) resource).isEmpty() && resource.equals("*")) {
                return false;
            }
            if (resource instanceof List<?> && ((List<?>) resource).contains("*")) {
                return false;
            }
        }
        return true;
    }

    private boolean processValidation(String json) throws JSONValidationException {
        if (json == null || json.trim().isEmpty()) {
            throw new JSONValidationException("Empty or null JSON string provided.");
        }
        JSONRolePolicy jsonRolePolicy;
        try {
            jsonRolePolicy = JSONRolePolicyMapper.deserializeJSON(json);
        } catch (RuntimeException e) {
            throw new JSONValidationException("An error occurred during JSON deserialization: " + e.getMessage(), e);
        }
        return validateResource(jsonRolePolicy);
    }

    public boolean validateJSONFromString(String json) throws JSONValidationException {
        return processValidation(json);
    }

    public boolean validateJSONFromFile(String filePath) throws IOException, JSONValidationException {
        String json = FileReaderUtil.readFileAsString(filePath);
        if (json.trim().isEmpty()) {
            throw new JSONValidationException("File content is empty.");
        }
        return validateJSONFromString(json);
    }

    public boolean isFilePath(String input) {
        File file = new File(input);
        return file.exists() && !file.isDirectory();
    }
}
