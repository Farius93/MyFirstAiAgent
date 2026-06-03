# Research Agent

A Java command-line research agent powered by the Anthropic API.

This project demonstrates a small AI workflow:
- prompt the user for a topic
- generate clarifying questions
- collect answers interactively
- produce a structured Markdown research report
- save the report under `output/`

## Requirements

- Java 21
- Gradle
- Anthropic API access
- `ANTHROPIC_API_KEY` environment variable set

## Build

Windows PowerShell:

```powershell
./gradlew.bat build
```

## Run

Windows PowerShell:

```powershell
$env:ANTHROPIC_API_KEY = "your-api-key"
./gradlew.bat run
```

Or after building:

```powershell
java -jar build\libs\research-agent-1.0-SNAPSHOT.jar
```

## Example

A saved example interactive session and generated report are included in:

- `examples/why-sky-is-blue/interaction.txt`
- `examples/why-sky-is-blue/report.md`

This keeps the example visible in the repository while preserving `output/` for runtime-generated reports.

## Project structure

- `build.gradle.kts` — Gradle build and application configuration
- `src/main/java/com/example/agent` — main application code
- `src/main/java/com/example/agent/util/SlugUtils.java` — safe filename slug generation
- `output/` — ignored runtime output directory
- `examples/` — reviewed example run and report

## Notes

- `output/` is ignored by Git, so generated reports are not committed automatically.
- Use the included example files to understand expected program behavior and report format.
