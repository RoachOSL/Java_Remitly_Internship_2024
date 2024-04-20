package dev.Roach;

import dev.Roach.exceptions.JSONValidationException;
import dev.Roach.util.ValidateJSONRolePolicyUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineRunner {
    public static void main(String[] args) {
        ValidateJSONRolePolicyUtil validator = new ValidateJSONRolePolicyUtil();
        String jsonContent = null;

        if (args.length > 0) {
            String input = args[0].trim();
            if (input.startsWith("{") && input.endsWith("}")) {
                jsonContent = input;
            } else {
                jsonContent = loadJSONFromFile(input);
            }
        }

        if (jsonContent == null) {
            System.err.println("No valid JSON input provided.");
            System.exit(2);
        }

        try {
            boolean validationResult = validator.validateJSONFromString(jsonContent);
            System.out.println("Validation result: " + validationResult);
        } catch (JSONValidationException e) {
            System.err.println("JSON validation error: " + e.getMessage());
            System.exit(3);
        }
    }

    private static String loadJSONFromFile(String filePath) {
        Path path = Paths.get(filePath);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            try {
                return new String(Files.readAllBytes(path));
            } catch (IOException e) {
                System.err.println("Failed to read from file: " + e.getMessage());
                return null;
            }
        } else {
            System.err.println("The provided file path does not exist or is a directory.");
            return null;
        }
    }
}
