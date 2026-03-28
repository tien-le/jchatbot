package com.learn4you.jchatbot.service;

//import com.google.genai.Client;
//import com.google.genai.types.GenerateContentConfig;
//import com.google.genai.types.GenerateContentResponse;
import com.learn4you.jchatbot.dto.request.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.google.genai.GoogleGenAiChatModel;
//import org.springframework.ai.google.genai.GoogleGenAiChatModel.ChatModel;
//import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(ChatRequest request) {
        System.out.println("input message: " + request.message());

//        return chatClient.prompt(request.message()).call().content();
        // https://docs.spring.io/spring-ai/reference/api/chatclient.html
        return this.chatClient
                       .prompt()
                       .user(request.message())
                       .call()
                       .content();
    }

}