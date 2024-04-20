package dev.Roach.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class FileReaderUtil {
    public static String readFileAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}