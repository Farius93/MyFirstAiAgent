package com.example.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversationOrchestrator {

    private static final Pattern QUESTION_PATTERN = Pattern.compile("^\\d+\\.\\s+(.+)$");

    private final Scanner scanner;

    public ConversationOrchestrator(Scanner scanner) {
        this.scanner = scanner;
    }

    public List<String> parseQuestions(String text) {
        List<String> questions = new ArrayList<>();
        for (String line : text.split("\n")) {
            Matcher m = QUESTION_PATTERN.matcher(line.trim());
            if (m.matches()) {
                questions.add(m.group(1).trim());
            }
        }
        return questions;
    }

    public List<String> collectAnswers(List<String> questions) {
        System.out.println("\n--- Clarifying Questions ---\n");
        List<String> answers = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            System.out.println((i + 1) + ". " + questions.get(i));
            System.out.print("   Your answer: ");
            System.out.flush();
            answers.add(scanner.nextLine().trim());
            System.out.println();
        }

        return answers;
    }
}
