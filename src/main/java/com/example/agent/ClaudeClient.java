package com.example.agent;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlock;

import java.util.List;
import java.util.stream.Collectors;

public class ClaudeClient {

    private static final Model MODEL = Model.of("claude-sonnet-4-6");

    private static final String CLARIFICATION_SYSTEM_PROMPT = """
            You are a research assistant helping to scope a research request before conducting
            in-depth research. Your only task right now is to ask clarifying questions.

            When given a research topic, respond with ONLY a numbered list of 3 to 5 clarifying
            questions that will help you understand the scope, audience, time period, depth, and
            specific aspects the user cares about. Ask nothing else. Do not provide any research
            yet. Format exactly like this:

            1. [Question one]
            2. [Question two]
            3. [Question three]
            """;

    private static final String RESEARCH_SYSTEM_PROMPT = """
            You are an expert research analyst with access to real-time web search.
            Produce a thorough, well-structured research report in Markdown format.

            Your report MUST include the following sections:

            # [TOPIC] - Research Report

            ## Executive Summary
            2-3 paragraph overview of the key findings.

            ## Background
            Context and history relevant to the topic.

            ## Key Findings
            Subsections covering the most important aspects discovered during research.

            ## Current Developments
            Recent news, trends, or changes (use web search for up-to-date information).

            ## Analysis
            Synthesized interpretation of the findings.

            ## Conclusion
            Summary of insights and recommended next steps.

            ## Sources
            A bulleted list of all sources used, with URLs.

            Use Markdown formatting (bold, bullet lists, headers) to make the report readable.
            Be factual and cite sources throughout.
            """;

    private final AnthropicClient client;

    public ClaudeClient(AnthropicClient client) {
        this.client = client;
    }

    public String askForClarifications(String topic) {
        MessageCreateParams params = MessageCreateParams.builder()
                .model(MODEL)
                .maxTokens(512L)
                .system(CLARIFICATION_SYSTEM_PROMPT)
                .addUserMessage("Research topic: " + topic)
                .build();

        Message response = client.messages().create(params);
        return extractText(response);
    }

    public String researchWithWebSearch(String topic, List<String> questions, List<String> answers) {
        String userPrompt = buildResearchPrompt(topic, questions, answers);

        MessageCreateParams params = MessageCreateParams.builder()
                .model(Model.of("claude-haiku-4-5"))
                .maxTokens(4096L)
                .system(RESEARCH_SYSTEM_PROMPT)
                .addUserMessage(userPrompt)
                .build();

        Message response = client.messages().create(params);
        return extractText(response);
    }

    private String buildResearchPrompt(String topic, List<String> questions, List<String> answers) {
        StringBuilder sb = new StringBuilder();
        sb.append("Research topic: ").append(topic).append("\n\n");
        sb.append("The user has answered the following clarifying questions to define the scope:\n\n");

        for (int i = 0; i < questions.size(); i++) {
            sb.append("Q").append(i + 1).append(": ").append(questions.get(i)).append("\n");
            if (i < answers.size()) {
                sb.append("A").append(i + 1).append(": ").append(answers.get(i)).append("\n");
            }
            sb.append("\n");
        }

        sb.append("Please conduct thorough web research on this topic given these constraints ");
        sb.append("and produce the full Markdown report as instructed.");
        return sb.toString();
    }

    private String extractText(Message message) {
        return message.content().stream()
                .flatMap(block -> block.text().stream())
                .map(TextBlock::text)
                .collect(Collectors.joining("\n"));
    }
}
