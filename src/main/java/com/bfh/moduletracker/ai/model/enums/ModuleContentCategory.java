package com.bfh.moduletracker.ai.model.enums;


import com.bfh.moduletracker.ai.service.ai.CategoryPromptProvider;

public enum ModuleContentCategory implements CategoryPromptProvider {

    COMPETENCIES {
        @Override
        public String getSubPrompt() {
            return """
                    Identify only the most relevant competencies from the module’s 'competencies' field - these are the specific skills or outcomes students achieve upon completion. \s
                    Extract only those competencies that are either: frequently mentioned across multiple modules, and/or highly relevant to the overall profile of the module. \s
                    Use concise phrases to describe each competency always with a verb-ending word, preferably using the original German terms as they appear in the text. \s
                    Assign a relative weight (0 to 1) to each competency based on its relevance to the specific module and frequency. \s
                    Ensure that the total sum of all weights equals 1. The total number of competencies should not exceed 8. \s
                    """;
        }
    },
    CONTENT {
        @Override
        public String getSubPrompt() {
            return """
                    Identify only the most relevant topics focuses from the module’s 'content' field – these are the main contents covered in the module. \s
                    Extract only those topics that are either: frequently mentioned across multiple modules, and/or highly relevant to the overall profile of the module. \s
                    Use short concise phrases to describe each topic, preferably using the original German terms as they appear in the text. \s
                    Assign a relative weight (between 0 and 1) to each topic based on its relevance to the specific module and frequency. \s
                    The sum of all weights should equal to 1. The total number of topics should not exceed 8. \s
                    """;

        }
    },

    REQUIREMENTS {
        @Override
        public String getSubPrompt() {
            return """
                    Identify only the most relevant requirements from the module’s 'requirements' field - these are the essential prerequisites for successful completion. \s
                    Extract only those requirements that are either: frequently mentioned across multiple modules, and/or highly relevant to the overall profile of the module. \s
                    Use short concise phrases to describe each requirement, preferably using the original German terms as they appear in the text. \s
                    Assign a relative weight (between 0 and 1) to each requirement based on its relevance to the specific module and frequency. \s
                    The sum of all weights should equal 1. The total number of requirements should not exceed 5. \s
                    """;
        }
    },

    SHORT_DESCRIPTION {
        @Override
        public String getSubPrompt() {
            return """
                    Identify only the most relevant themes focuses from the module’s 'shortDescription' field – this section provides a general summary of what the module is about. \s
                    Extract only those themes that are either: frequently mentioned across multiple modules, and/or highly relevant to the overall profile of the module. \s
                    Use short concise phrases to describe each theme without a verb-ending word, preferably using the original German terms as they appear in the text. \s
                    Assign a relative weight (between 0 and 1) to each topic based on its relevance to the specific module and frequency. \s
                    Ensure the total sum of all weights equals 1. The total number of key themes should be grater than 4 but not exceed 8. \s
                    """;
        }
    },

    RESPONSIBILITY {
        @Override
        public String getSubPrompt() {
            return """
                    Extract the name of the person responsible for the module from the module’s 'responsibility' field. \s
                    Distinguish clearly between first names and surnames.\s
                    The names come from Switzerland (German and French-speaking) and follow local naming conventions.                     Return the name in the format: '[First Initial]. [Surname]'. \s
                    Do not include usernames, IDs, or abbreviations. \s
                    If multiple names are present, return all of them. \s
                    """;
        }
    },

    ECTS {
        @Override
        public String getSubPrompt() {
            return """
                    - CONTENT CATEGORY ECTS: \s
                    Identify the value of ects from the module’s 'ects' field. \s
                    """;
        }
    },

    SELF_STUDY {
        @Override
        public String getSubPrompt() {
            return """
                    - CONTENT CATEGORY SELF STUDY: \s
                    Extract the value of self study from the module’s SELF STUDY SECTION. \s
                    """;
        }
    },
}





