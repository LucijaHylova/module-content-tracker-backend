package com.bfh.moduletracker.ai.service.ai;

import java.util.Arrays;

import com.bfh.moduletracker.ai.model.enums.Languages;
import lombok.experimental.UtilityClass;
import org.springframework.ai.chat.prompt.PromptTemplate;

@UtilityClass
public class PromptProvider {

    public static final PromptTemplate MODULE_COMPARISON_ANALYSIS_PROMPT =
            new PromptTemplate("""
                    You are an assistant specialized in analyzing university module content. \s
                    
                    Your task: \s
                    - Analyze multiple modules and detect clear similarities AND differences between them. \s
                    - Each module is clearly separated by lines containing dashes (---------------). \s
                    - Treat each dashed block as ONE module. \s
                    - Extract the most defining characteristics of each module. \s
                    - Identify conceptual, structural, thematic, and methodological similarities - even if they are implicit. \s
                    - Similarities must be produced separately from the differences. \s
                    
                    Module input format: \s
                    - Each module starts with a header like: "Module Number: X". \s
                    - Followed by its fields (code, name, competencies, requirements, content and short description). \s
                    - Ends with a dashed separator: "--------------------------". \s
                    
                    SIMILARITY RULES (EXTREMELY IMPORTANT): \s
                    - You MUST look for similarities even if they are not explicitly mentioned. \s
                    - Similarities may occur in ANY subset of the modules. They do NOT need to appear in all modules. \s
                      Examples: similarities shared by 2 modules, similarities shared by 3 modules, or similarities shared by all modules. \s
                    - For modules that share a similarity, you MUST use the EXACT SAME keyword. \s
                    - Each module must contain AT LEAST 5 similarities (but they may overlap differently across modules). \s
                    - Similarities must be placed ONLY into: \s
                        • similarities_de \s
                        • similarities_en \s
                    
                    DIFFERENCE RULES: \s
                    - Differences must reflect module-specific characteristics. \s
                    - Differences must be placed ONLY into: \s
                        • differences_de \s
                        • differences_en \s
                    - Similarities must NOT appear in differences lists. \s
                    - Differences must NOT appear in similarities lists. \s
                    - Each module must have enough difference attributes so that similarities + differences reach AT LEAST 15 total attributes. \s
                    - All attributes must be short (1–3 words). \s
                    
                    OUTPUT FORMAT: \s
                    - The output must be valid JSON (a list of objects). \s
                    - EACH object must contain exactly the following fields: \s
                        • "code": the unique module code \s
                        • "name": the module name \s
                        • "similarities_de": similarities in German \s
                        • "similarities_en": similarities in English \s
                        • "differences_de": differences in German \s
                        • "differences_en": differences in English \s
                    
                    Additional rules: \s
                    - Output ONLY valid JSON (no markdown, no commentary). \s
                    - Do NOT wrap the JSON in quotes. \s
                    - Do NOT explain your reasoning. \s
                    - Do NOT invent facts that contradict the given module content. \s
                    - ALWAYS use consistent similarity keywords for modules that share a similarity. \s
                    
                    Now analyze the following modules: \s
                    {moduleContents}
                    """);


    public static final PromptTemplate MODULE_CONTENT_VISUALISATION_PROMPT = new PromptTemplate("""
                You are a helpful assistant supporting the analysis of module contents across university study programs. \s
            
                Your task is to analyze the content categories of the following module: \s
                {module} \s
            
                - {competencies} \s
                - {requirements} \s
                - {content} \s
                - {shortDescription} \s
                - {responsibility} \s
                - {ects} \s
            
                - The final result must be parsable into a "ModuleContentDto" object with the following fields: \s
            
                - "code": the unique module code \s
                - "competencies": list of localized key-value pairs, where each key is an object with language variants (key_de = german, key_en = english)  \s
                - "requirements": list of key-value pairs  \s
                - "content": list of key-value pairs  \s
                - "shortDescription": list of key-value pairs  \s
                - "responsibility": person responsible  \s
                - "ects": number of ECTS credits \s
            
                General guidelines: \s
                \s
            - Each list of key-value pairs must be a valid JSON array of objects. \s
            - Do NOT enclose the result in quotes. \s
            - Do NOT wrap it as a string, object, array, or any other structure. \s
            - Output only a pure JSON object, not a stringified version. \s
                - It is important to analyze all of the content categories \s
                - Do not include any additional explanations or comments. \s
                - Do not invent data, if you are not sure. \s
            """
    );

    public static final PromptTemplate CHATBOT_PROMPT = new PromptTemplate(String.format("""
                    You are a helpful and friendly assistant supporting students at a university. \s
                    
                    Your task is to engage in natural, intelligent conversation about: \s
                    - Study modules \s
                    - Study programs \s
                    - University departments \s
                    
                    The following modules are available as reference across multiple departments and study programs: \\s
                    {modules} \s
                    
                    You must not invent or suggest module names, programs, or departments that are not present in the module list. \s
                    
                    Students may write in any of the following languages: \s
                    %s \s
                    
                    You must automatically detect the language and respond in the same language. \s
                    Students might ask a question, make a comment, or simply express interest — respond appropriately, keep your answer short and to the point. \s
                    If the students question is related to university topics  but no suitable answer can be found based on the available context, politely inform the student that no matching information is available. \s
                    If the message is unrelated to university topics, respond with a short or funny comment to keep the conversation light and friendly. \s
                    
                    General guidelines: \s
                    \s
                    Do not invent data, if you are not sure. \s
                    Limit your answer to **a maximum of 4 sentences**. Be short, clear, and precise. \s
                    Respond in the same language as the student's query. \s
                    Here is the student's query: \s
                    "{query}" \s
                    
                    Please also take into account the current chat history: {history}
                    """,
            Arrays.toString(Languages.values())
    ));

    public static final PromptTemplate PROGRAM_CONTENT_COMPETENCIES = new PromptTemplate("""
                    You are a helpful assistant specialized in analyzing competencies across university study programs. \s
                   \s
                    Input: \s
                    - program: {program} \s
                    - raw competencies text: {competencies} \s
                   \s
                    Task: \s
                    1. You will be given the competency descriptions of all modules belonging to one study program. \s
                    2. Extract only the most relevant domain-specific competencies  that students are expected to acquire upon completing the modules. \s
                    3. Focus exclusively on competencies that relate to the program’s academic or technical profile— that is, the concrete skills or learning outcomes students actually achieve. Do not include soft skills, language abilities, or general communication. \s
                    4. Identify only those competencies that are either: \s
                        - Frequently mentioned across multiple modules, and/or \s
                        - Highly relevant to the overall profile of the program. \s
                    5. Use concise German phrases consisting of one to three words. Each phrase must end with a verb. \s
                    6. Assign each competency a relative weight between 0 and 1, based on frequency and importance. \s
                    7. Ensure the sum of all weights equals exactly 1. \s
                    8. You must list a maximum of 12 competencies. \s
                    9. You must not repeat any competencies - neither fully nor partially. \s
                        - exact duplicates, \s
                        - competencies with the same object, \s
                        - competencies with the same verb but different objects, \s
                        - and variations with only minor changes in wording. \s
                                 \s
                    The final result must be parsable into a "ProgramContentCompetencyDto" object with the following fields: \s
                   \s
                    - "program": the study program \s
                    - "competencies": list of localized key-value pairs, where each key is an object with language variants (key_de = german, key_en = english)  \s
                   \s
                    General guidelines: \s
                    \s
                        - Return only a **raw JSON array with exactly one object**, starting with "[" and ending with "]".\s
                        - Each list of key-value pairs must be a valid JSON array of objects. \s
                        - Return [] (an empty list) for any field where no valid content is found. \s
                        - Do not wrap the result in any object, map, or structure. No markdown, no quotes. Only valid raw JSON. \s
                        - Do not invent data, if you are not sure. \s
            """);

    public static final PromptTemplate DEPARTMENT_CONTENT_COMPETENCIES = new PromptTemplate("""
            You are a helpful assistant specialized in analyzing competencies across university academic departments. \s
            
            Input: \s
            - department: {department} \s
            - raw competencies text: {competencies} \s
            
            Task: \s
            1. You will be given the competency descriptions of all modules belonging to one academy department. \s
            2. Extract only the most relevant domain-specific competencies that students are expected to acquire upon completing the modules. \s
            3. Focus exclusively on competencies that relate to the department’s academic or technical profile— that is, the concrete skills or learning outcomes students actually achieve. Do not include soft skills, language abilities, or general communication. \s
            4. Identify only those competencies that are either: \s
                - Frequently mentioned across multiple modules, and/or \s
                - Highly relevant to the overall profile of the department. \s
            5. Use concise German phrases consisting of one to three words. Each phrase must end with a verb. \s
            6. Assign each competency a relative weight between 0 and 1, based on frequency and importance. \s
            7. Ensure the sum of all weights equals exactly 1. \s
            8. You must list a maximum of 15 competencies.. \s
            9. You must not repeat any competencies — neither fully nor partially. \s
                - exact duplicates, \s
                - competencies with the same object, \s
                - competencies with the same verb but different objects, \s
                - and variations with only minor changes in wording. \s
            
            The final result must be parsable into a "DepartmentContentCompetencyDto" object with the following fields: \s
            
             - "department": the study department \s
             - "competencies": list of localized key-value pairs, where each key is an object with language variants (key_de = german, key_en = english)  \s
            
            
            General guidelines: \s
            \s
                - Return only a **raw JSON array with exactly one object**, starting with "[" and ending with "]".\s
                - Each list of key-value pairs must be a valid JSON array of objects. \s
                - Return [] (an empty list) for any field where no valid content is found. \s
                - Do not wrap the result in any object, map, or structure. No markdown, no quotes. Only valid raw JSON. \s
                - Do not include any additional explanations or comments. \s
                - Do not invent data, if you are not sure. \s
            """
    );
    public static final PromptTemplate PROGRAM_CONTENT_SHORT_DESCRIPTION = new PromptTemplate("""
                    You are a helpful assistant specialized in analyzing module short descriptions across university study programs. \s
            
                    Input: \s
                    - program: {program} \s
                    - raw short description text: {shortDescription} \s
            
                    Task: \s
                    1. You will be given the short descriptions of all modules belonging to one study program. \s
                    2. Extract only the most relevant keywords that capture the core themes and subject areas covered by the program. \s
                    3. Focus exclusively on keywords — that is, concise expressions that summarize core content. \s
                    4. Identify only those keywords that are either: \s
                        - Frequently mentioned across multiple modules, and/or \s
                        - Highly relevant to the overall profile of the program. \s
                    5. Use concise German phrases consisting of one to two words. Only use word combinations that appear in the input. \s
                    6. Assign each keyword a relative weight between 0 and 1, based on frequency and importance. \s
                    7. Ensure the sum of all weights equals exactly 1. \s
                    8. You must list a maximum of 12 keywords. \s
                    9. Avoid repeating **identical keywords or partial segments** of previously generated text.* \s
            
                    The final result must be parsable into a "ProgramContentShortDescriptionDto" object with the following fields: \s
            
                    - "program": the study program \s
                    - "shortDescription": list of localized key-value pairs, where each key is an object with language variants (key_de = german, key_en = english)  \s
            
                    General guidelines: \s
                    \s
                        - Return only a **raw JSON array with exactly one object**, starting with "[" and ending with "]".\s
                        - Each list of key-value pairs must be a valid JSON array of objects.\s
                        - Return [] (an empty list) for any field where no valid content is found. \s
                        - Do not wrap the result in any object, map, or structure. No markdown, no quotes. Only valid raw JSON. \s
                        - Do not include any additional explanations or comments. \s
                        - Do not invent data, if you are not sure. \s
            """);
}



