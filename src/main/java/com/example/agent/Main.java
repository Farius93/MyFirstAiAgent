package com.example.agent;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Error: ANTHROPIC_API_KEY environment variable is not set.");
            System.exit(1);
            return;
        }

        AnthropicClient client = AnthropicOkHttpClient.fromEnv();
        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("        Research Agent          ");
        System.out.println("================================");
        System.out.println();
        System.out.print("Enter research topic: ");
        System.out.flush();
        String topic = scanner.nextLine().trim();

        if (topic.isEmpty()) {
            System.err.println("Error: Topic cannot be empty.");
            System.exit(1);
            return;
        }

        ClaudeClient claudeClient = new ClaudeClient(client);

        System.out.println("\nGenerating clarifying questions...");
        String questionsText = claudeClient.askForClarifications(topic);

        ConversationOrchestrator orchestrator = new ConversationOrchestrator(scanner);
        List<String> questions = orchestrator.parseQuestions(questionsText);

        if (questions.isEmpty()) {
            System.err.println("Error: Could not parse clarifying questions from response.");
            System.err.println("Response was: " + questionsText);
            System.exit(1);
            return;
        }

        List<String> answers = orchestrator.collectAnswers(questions);

        System.out.println("Researching with web search... (this may take 30-60 seconds)");
        String report = claudeClient.researchWithWebSearch(topic, questions, answers);

        try {
            Path outputPath = ReportWriter.write(topic, report);
            System.out.println("\nReport saved to: " + outputPath);
            System.out.println("Done!");
        } catch (IOException e) {
            System.err.println("Error: Could not write report to output/. " + e.getMessage());
            System.exit(1);
        }
    }
}
