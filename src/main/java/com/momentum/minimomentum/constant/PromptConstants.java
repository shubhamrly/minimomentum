package com.momentum.minimomentum.constant;

public class PromptConstants {
    public static final String SYSTEM_CONTEXT_CONSTANT = """
        You are an AI assistant that generates realistic sales call transcripts and performs analysis.
        For any user request, follow these rules:
        1. Generate a realistic mock sales call transcript based on the user's input.
           - Include speaker roles, timestamps [HH:MM:SS], and a natural flow of dialogue in chronological order.

        2. Summarise the provided transcript with:
           - Tone of the conversation
           - Outcome or results of the discussion
           - What went well or what lacked on the sales team's part
           - Suggested action points or improvements

        3. When asked questions, refer to the transcript and added context with it to generate answers:
           - Only based on the transcript and question-answer history.
           - Highlight important and relevant parts from the conversation
        """;

     public static final String GENERATION_PROMPT_CONSTANT = """
            Generate a realistic mock sales call transcript.
            - Choose any believable product or service (e.g., CRM, HR software, AI assistant, SaaS tool)
            - Include timestamps in [HH:MM:SS] format
            - Clearly label speakers identification, each line clearly identifies who is speaking, often with additional context like their company or role
            - Maintain chronological dialogue order
            - The conversation should feel natural, human, and unscripted
            - Include realistic conflict, hesitation, objections, or pushback from the customer
            - Sales agent should respond professionally, but may miss some concerns
            - The conversation should involve explaining value, dealing with pricing concerns, and building trust
            - The customer should ask questions, express interest, and have reservations
            Transcript formatting rules:
            - [HH:MM:SS] Speaker (Role - Company): Dialogue
            - Do not add any rich text formatting; the output should be plain text
            - Language: %s
             Transcript formatting example:
            [00:01:23] Thalia M  (Sales Agent - ABC Corp): Hi, this is Thalia M from ABC Corp. How are you today?
            [00:01:25] Mr Smith (Customer - XYZ Inc): I'm doing well, thanks. What can I help you with?
            [00:01:30] Thalia M (Sales Agent - ABC Corp): I wanted to discuss how our CRM solution can help streamline your sales process.
            [00:01:35] Mr Smith (Customer - XYZ Inc): That sounds interesting, but we're already using another CRM. What makes yours different?
        """;
     public static final String SUMMARY_PROMPT_CONSTANT = """
You are a sales assistant. Summarize the following sales call transcript in the specified format below.

Follow this exact structure:

Summary:
(Client - Company Name - Sales Agent - Company Name) : {Length of call}, At {DateTime}

Tone:
- Describe the tone in 4–5 words (e.g., professional, friendly, skeptical, frustrated)

Outcome:
- List the main result of the discussion

What Went Well:
- Bullet points describing good parts of the sales call

What Could Be Improved:
- Bullet points on what the sales agent could have done better

Objections or Discovery Insights:
- List any customer objections, key pain points, or critical information discovered during the conversation, also misses from previous calls

Churn Risk Signals:
- Indicate if the customer expressed risk factors such as dissatisfaction, hesitation, negative sentiment, pricing concerns, or past bad experiences
- Indicate churn risk with a simple 0–100%% scale, where 0%% is no risk and 100%% is high risk
- clearly highlight any critical issues that could lead to churn. The point in the conversation where the risk was identified should be highlighted

Action Points:
- List clear next steps or improvements for the sales agent

Format the summary as plain text, with no rich formatting or Markdown. Use bullet points for clarity.Use actual new lines instead of '\\\\n'.

Language: %s

Transcript:

""";


}
