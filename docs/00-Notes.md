
mvn versions:display-dependency-updates

# Google Gen AI Java SDK
https://github.com/googleapis/java-genai

# Exercise 

## BT0: Config the Spring AI

## BT1: Chat with simple text, output text

## BT2: Chat with image and text, output text

## BT3: Chat with text, output is JSON - list of Record (film information)

## BT4: Chat with image, text, output is JSON - List of products that are bought

## BT5: Chat memory with Conversation ID

## BT6: Show DEBUG information of ChatMemory
Note: Table "spring_ai_chat_memory"
Columns:
    conversation_id: varchar(36)
    content: text
    type: varchar(10)
    timestamp(timestamp)

Example:
    Conversation-01, What is your name? What is your job?, USER/ASSISTANT, 2026-03-29 20:20:46.000

Ref: https://github.com/spring-projects/spring-ai/blob/main/memory/repository/spring-ai-model-chat-memory-repository-jdbc/src/main/resources/org/springframework/ai/chat/memory/repository/jdbc/schema-postgresql.sql