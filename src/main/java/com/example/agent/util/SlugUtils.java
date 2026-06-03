package com.example.agent.util;

public class SlugUtils {

    public static String toSlug(String topic) {
        String slug = topic.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        return slug.substring(0, Math.min(50, slug.length()));
    }
}
