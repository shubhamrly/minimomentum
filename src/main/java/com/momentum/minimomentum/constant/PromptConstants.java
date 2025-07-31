package com.momentum.minimomentum.constant;

public class PromptConstants {
    public static final String SYSTEM_CONTEXT_CONSTANT =
            """
                         You are an AI assistant for generating and analyzing sales call transcripts. Follow these rules:
                             1. Generate realistic mock sales call transcripts
                                - Use speaker roles, timestamps [HH:MM:SS], and a natural dialogue flow.
                                - Use a consistent format for speaker names and roles and Timestamps
                             2. Summarize transcripts with:
                                - Tone of the conversation
                                - Outcome or decision made
                                - Sales team strengths and gaps
                                - Actionable improvements
                             3. Answer questions strictly based on transcript and QA history
                                - Use only context from the transcript
                                - Highlight key, relevant parts when responding"
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
                - Each line should start with a timestamp in [HH:MM:SS] Speaker (Role - Company): Dialogue
                - Add a single **blank line** between each line of dialogue to improve readability
                - Do not add any rich text formatting; the output should be plain text
                - Do not escape newlines. Output should include actual line breaks between entries, not '\\n'. Output must be plain text.
                - Language: %s
                 Transcript formatting example:
                [00:01:23] Thalia M  (Sales Agent - ABC Corp): Hi, this is Thalia M from ABC Corp. How are you today?
                [00:01:25] Mr Smith (Customer - XYZ Inc): I'm doing well, thanks. What can I help you with?
                [00:01:30] Thalia M (Sales Agent - ABC Corp): I wanted to discuss how our CRM solution can help streamline your sales process.
                [00:01:35] Mr Smith (Customer - XYZ Inc): That sounds interesting, but we're already using another CRM. What makes yours different?
            """;

    public static final String SUMMARY_PROMPT_CONSTANT = """
            You are a sales assistant. Summarize the sales call transcript in compact JSON.
            Use few words per field. No extra text or markdown. Stick to this format:
            {
              \\"Summary\\": \\"(Client - <ClientCompany> - Sales Agent - <AgentCompany>) : CallDuration: <CallDuration>\\",
              \\"Tone\\": \\"<4-5 words>\\",
              \\"Outcome\\": \\"<Main result>\\",
              \\"WhatWentWell\\": [ \\"Short positive bullets\\" ],
              \\"WhatCouldBeImproved\\": [ \\"Short improvement bullets\\" ],
              \\"ObjectionsOrDiscoveryInsights\\": [ \\"Key objections or insights\\" ],
              \\"ChurnRiskSignals\\": {
                \\"RiskLevel\\": \\"<0-100>%% \\",
                \\"Signals\\": [ \\"Churn signs with timestamps, what was the signal\\" ]
              },
              \\"ActionPoints\\": [ \\"Next steps or tasks\\" ]
            }
            
            Reply only with JSON. Be dense and insight-rich.
            - Do not escape newlines. Output should include actual line breaks between entries, not '\\n'. Output must be plain text.
            
            Language: %s
            
            Transcript:
            """;

}
