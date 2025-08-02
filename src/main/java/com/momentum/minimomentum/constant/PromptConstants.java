package com.momentum.minimomentum.constant;


//This file contains constants for prompts used in the application.
public class PromptConstants {

    //System context for the AI assistant
    // This context provides guidelines for the AI's behavior and response style during sales call transcript generation
    public static final String SYSTEM_CONTEXT_CONSTANT = """
                 -You are an AI assistant for sales call transcripts.
                 -When asked to generate call: Generate realistic calls with timestamps [HH:MM:SS], roles, and natural flow.
                 -To summarise: Summarize only when asked, with insights, objections, churn risks.
                 -To Answer Question: Answer questions strictly from transcript and QA history only—no external info.
                 -Imp: Highlight only relevant parts.
            """;

    //Prompts for generating sales call transcripts. Space sanitization will be in the service layer.

    public static final String GENERATION_PROMPT_CONSTANT = """
                Generate a realistic sales call transcript between a <productName> software product (CRM, HRM, IMS,HM,etc) salesperson and a retail client.
                - Include timestamps in [HH:MM:SS] <Person,company> format, maintain chronological order,
                - include any product,service, use natural flow, let speaker have doubts, intersets,objections.
                - the conversation ends naturally, complete the transcript in 12-15 lines (6–8 per person).
                - Language: %s
                 Transcript formatting example:
                [00:01:23] Thalia M  (Sales Agent - ABC Corp): Hi, this is Thalia M from ABC Corp. How are you today?
                [00:01:25] Mr Smith (Customer - Manager XYZ Inc): I'm doing well, thanks. What can I help you with?
                [00:01:30] Thalia M (Sales Agent - ABC Corp): I wanted to discuss how our CRM solution can help streamline your sales process.
            """;
    // This prompt is used to summarize the sales call transcript in a fixed structured JSON format.

    public static final String SUMMARY_PROMPT_CONSTANT = """
            You are a sales assistant. Summarize the sales call transcript in compact JSON.
            - Language for response: %s . No matter the transcript language, always respond in this target language.
            - If the language is not supported, fallback to English.
            Use few words per field. No extra text or markdown. Stick to this format:
            {
               \\"summary\\": "\\Summary of the call in information rich sentence, concise .Min 100,Max 250 words, dont count other sections words in this sections. \\",
               \\"summaryDetails\\": {
                 \\"agent\\": \\"<Name of the agent>\\",
                 \\"customer\\": \\"<Customer name, Company>\\",
                 \\"tone\\": \\"<4-5 words>\\",
                 \\"outcome\\": \\"<Main result>\\",
                 \\"strengths\\": [ \\"Short positive bullets\\" ],
                 \\"improvements\\": [ \\"Short improvement bullets\\" ],
                 \\"insights\\": [ \\"Key objections or insights\\" ],
                 \\"actionPoints\\": [ \\"Next steps or tasks\\" ],
                \\ "churnRiskSignals\\": {
                   \\"riskLevel\\": \\"<0-10>\\",(0-10 scale, 0 is no risk, 10 is high risk)
                   \\"signals\\": [ \\"Churn signs with timestamps, what was the signal\\" ]
                 }
               }
             }
            Summary Formatting rules:
            Reply only with JSON. Be dense and insight-rich.
         
            Transcript:
            """;

    //For question and answer generation on transcripts.
    public static final String QUESTION_ANSWER_PROMPT_CONSTANT = """
        You are a sales assistant. Answer the question based on the provided transcript.
        - Use only the information from the transcript and question and answer history available with it to answer the question.
        - If history is available, use it to answer the question. The questions are sorted according to the latest by createdDatetime parameter.
        - Answer in under 100 words.
        - If the question mentions a language, answer in that language.
        """;

}
