# DIO Spring Boot - Final Project 05: Spring AI (budgeting)

## Entrega do desafio DIO

Esta API registra e consulta despesas por categoria. No fluxo de voz, o Spring AI transcreve o áudio em português, o ChatClient usa Tool Calling para consultar ou gravar no MySQL e o modelo de voz devolve a resposta em MP3.

A melhoria valida descrição, valor positivo em centavos e categoria no caso de uso de gravação, compartilhado pela API REST e pelo assistente. A descrição é aparada e limitada a 255 caracteres. Uma gravação REST inválida recebe HTTP 400 com o motivo da rejeição.

Tecnologias: Java 25, Spring Boot, Spring AI, OpenAI, Spring Data JPA, MySQL, Docker Compose e Gradle. Base do laboratório: [DIO Spring Boot Learning Track](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/tree/9add434/05-spring-ai).

### Executar e testar no Windows

Na raiz do repositório, com JDK 25, Docker em execução e uma chave OpenAI já disponível na variável de ambiente `OPENAI_API_KEY`, execute:

```powershell
.\gradlew.bat bootRun
```

O Spring Boot inicia o MySQL a partir de `compose.yml`. Use somente dados de exemplo para testar o fluxo de voz, pois o áudio e o texto são processados pelo provedor configurado.

```powershell
Invoke-RestMethod -Method Post -Uri 'http://localhost:8080/transactions' -ContentType 'application/json' -Body '{"description":"Mercado","category":"GROCERIES","amount":1250}'
Invoke-RestMethod -Uri 'http://localhost:8080/transactions/GROCERIES'
curl.exe --fail-with-body -F "file=@src/test/resources/audio/recording-1.m4a" http://localhost:8080/transactions/ai -o resposta.mp3
```

O primeiro comando registra uma despesa de R$ 12,50 e o segundo lista as despesas da categoria. O terceiro grava a resposta do assistente em `resposta.mp3`. Repita o POST JSON com `amount` igual a zero para verificar a resposta HTTP 400.

### Aprendizado técnico

O exemplo mostra como usar o modelo para interpretar o comando e selecionar uma função, mantendo as regras de negócio no caso de uso Java. Colocar a validação nessa camada permite aplicar as mesmas regras a entradas de voz e JSON.

## Introduction

This final module applies Spring AI in a budgeting API while preserving the same layered architecture used across the track.

The goal is to integrate AI capabilities without bypassing domain and use case boundaries.

## Code Context

The project processes voice commands to create and query financial transactions.

Primary flow:

1. Client uploads an audio file.
2. Audio is transcribed into text.
3. The model selects an application tool/use case.
4. The use case persists or queries transaction data.
5. The final response is converted to audio.

## Project Structure

- `src/main/java/dio/budgeting/domain`
  - Domain model and repository contract.
- `src/main/java/dio/budgeting/application`
  - Use cases used by both REST and AI tool calling.
- `src/main/java/dio/budgeting/infrastructure`
  - HTTP adapters, JPA adapters, and integration glue.

## Module-Specific Topics

### Speech-to-text

- Uses `TranscriptionModel` for audio transcription.
- Model settings are configured in `application.properties`.

### Tool calling

- `ChatClient` registers use-case tools.
- `@Tool` methods expose business capabilities to the model.

### Text-to-speech

- `TextToSpeechModel` produces MP3 output from final text.
- AI endpoint returns generated audio.

## Spring AI Documentation

- Spring AI Reference: https://docs.spring.io/spring-ai/reference/index.html
- ChatModel API: https://docs.spring.io/spring-ai/reference/api/chatmodel.html
- ChatClient API: https://docs.spring.io/spring-ai/reference/api/chatclient.html
- Tools API: https://docs.spring.io/spring-ai/reference/api/tools.html
- Audio Transcriptions API: https://docs.spring.io/spring-ai/reference/api/audio/transcriptions.html
- Audio Speech API: https://docs.spring.io/spring-ai/reference/api/audio/speech.html

## Shared Architecture References

Common architecture concepts are documented in the root README:

- [DDD layers](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/9add434/README.md#ddd-layered-architecture)
- [Class vs record](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/9add434/README.md#java-class-vs-java-record-in-domain-modeling)
- [Strong typed identifiers](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/9add434/README.md#strong-typed-identifiers)
- [Repository pattern](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/9add434/README.md#repository-pattern)
- [Use cases and Clean Architecture](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/9add434/README.md#use-cases-and-clean-architecture)
- [Docker Compose support](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/9add434/README.md#docker-compose-support-in-development)

## How to Run

Set your OpenAI API key:

```bash
export OPENAI_API_KEY="your_api_key_here"
```

Run the application and tests:

```bash
./gradlew bootRun
./gradlew test
```

## Notes

- Educational final project focused on AI plus architectural discipline.
- External provider integration tests may require active credentials.
