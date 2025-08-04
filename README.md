# Mini-Momentum

Summarizes, ask questions from a transcript.

## Table of Contents

- [Thought Process](#thought-process)
- [System Diagram](#system-diagram)
- [Tech Stack](#tech-stack)
- [Environment Variables](#environment-variables)
- [Installation](#installation)
- [AI usage](#ai-usage)
- [Resources Used](#resources-used)
- [Sample Curl request](#sample-curl-request)

---

## Thought Process
## Approach

### Initial Planning
Broke down the assignment into three core functional modules:
  1. Transcript Generation Module – generates transcripts using OpenAI client.
  2. Summarization Module – Summarises the transcript using the OpenAI client.
  3. Question Answer Module – answers user questions contextually using transcript and prior Q&A history..
  4. OpenAI Integration:
      - Initially used manual WebClient calls.
      - Switched to `spring-ai-openai-spring-boot-starter` for abstraction and YAML-based configuration.

**Prompt Engineering:**
- Used OpenAI documentation and Playground to prompt design and optimization.
- The PromptConstants contain the System and user prompts for each module.
- Caveat: OpenAI's Chat Completion API is stateless ,therefore each request goes with System and user prompt.
---
### 1. Transcript Generation
- Used OpenAI Chat Completion API for generating transcript content,
- Since the Completion API is stateless, unique ID generation was handled internally using database primary keys.
- The language of the generated transcript is set to English by default, but can be customized via the `language` parameter in the request.

**Response Handling:**
- The OpenAI response contains excess metadata.
- Only the "content" key was extracted and used as the final transcript string.

---

### 2. Summarization Logic
- Reviewed `Momentum.io` summary styles and focused on:
  - `Action items`
  - `Pipeline risk`
  - `Churn indicators`
  - `Call insights`
- Designed the summary schema to capture these details with fields like `insights`, `objections`, and `churnRisks` , etc (useful in sync feature).

**Output Format:**
- Used JSON to ensure compatibility with tools like Salesforce that use SObjects, or any other sync service.
- The summary is structured as a `SummaryDTO` with fields for:
  - `transcriptId`
  - `language`
  - `summaryText`
  - `summaryDetails` (nested DTO for structured data)

**Storage Strategy:**
- Used a composite key `(transcriptId + language)` to enforce idempotency:
  - Existing summary → update
  - New summary → insert
- Persistence:
  - Full summary text in `summaryText`
  - Structured data in a nested `SummaryDetailsDTO`



---

### 3. Question Answering
- Used OpenAI Chat Completion API to generate answers for questions asked in the context to transcripts.
- The prompt for QA included, UserPrompt + Context (Transcript + Previous Q&A history) to keep the Completion API in context.
- The response to end user is given as: Answer: {Returned response from OpenAI}
- Internally all the QA are joined by its transcript ID and stored in the database.
- The question and answer language can be dynamically set via asking in that language or adding "respond in {lang} language" to the question.

## Persistence:
- All services have operated with entity via hibernate(JPA).
- For the persistence-file-based database, H2 embedded was the winning choice. 
- The database is at  `/data` directory within the application.
- The Relationship between Transcript to Summary and Question answer was `One-to-Many`.
- The summary is linked to the `Transcript` entity via a foreign key.
- The `QuestionAnswer` entity is linked to the `Transcript` entity via a foreign key.

**Average Response Time (Open AI):**

- Model: gpt-3.5-turbo, gpt-4o
- Transcript Generation: ~7–12 seconds
- Summarization: ~5–7 seconds
- Question Answering: ~1–3 seconds
---

## System Diagram

![Alt Text](assests/Sys_Diag_MiniMomentum.svg)

---
## Tech Stack
**Backend:**
- SpringBoot 

**Database:**
- SQL (H2: embedded)

**Testing:**
- Junit 5
- Mockito

**AI assistance:**

- Chatgpt for suggestion
- IDE suggestions.

---

## Installation
**Prerequisites:**
- Java 17 JDK
- Maven 3.8+
- OpenAI API key

## Environment Variables

### Required
```
| Variable         | Description                              |
|------------------|------------------------------------------|
| `OPENAI_API_KEY` | OpenAI key for Client service |
```
### Optional
```
| Variable       | Description                    
|----------------|--------------------------------
| `H2_USERNAME`  | Username for H2 embedded database access 
| `H2_PASSWORD`  | Password for H2 embedded database access 
````

**Installation**
- `git clone https://github.com/shubhamrly/minimomentum.git`

then in terminal,
``` 
  export OPENAI_API_KEY=your_api_key

```
else, 

- put it in the application configuration in intelliJ as 
 ```
Key: OPENAI_API_KEY
    
Value : {project-key}
```
-  then `mvn spring-boot:run` OR run button


-  use `mvn test` for testing

- server will avaialble on `localhost:8080`

- swagger will be avaiable on  `http://localhost:8080/swagger-ui/index.html`

- H2 database will be available at `http://localhost:8080/h2-console`


## Resources used

- OPEN AI playground: Chat Prompt service to refine prompts. 
- <a href="https://platform.openai.com/docs/api-reference/chat/create">OpenAI API Reference</a>
- <a href ="https://docs.spring.io/spring-ai/reference/getting-started.html"> Spring AI Documentation</a>
- <a href="https://spring.io/guides/gs/testing-web">Spring Testing Guide</a>
- <a href="https://github.com/Louis3797/awesome-readme-template/blob/main/SLIMMED-README-WITHOUT-EMOJI.md"> Readme Template</a>


## AI-usage

- The test cases skeleton is produced via gpt assistant, but the actual code by done by me.
- The application used suggestion by IDE.
- Some comments are autogenerated by AI, but the actual code is written by me.
- The AI-generated content has been marked with /** AI-Generated** /

# Sample Curl request
- refer to the file at parent level 'request.HTTP' for api requests.
