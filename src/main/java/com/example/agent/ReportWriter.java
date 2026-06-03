package com.example.agent;

import com.example.agent.util.SlugUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportWriter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static Path write(String topic, String content) throws IOException {
        String slug = SlugUtils.toSlug(topic);
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String filename = slug + "_" + timestamp + ".md";

        Path outputDir = Path.of("output");
        Files.createDirectories(outputDir);
        Path outputFile = outputDir.resolve(filename);
        Files.writeString(outputFile, content, StandardCharsets.UTF_8);
        return outputFile;
    }
}
