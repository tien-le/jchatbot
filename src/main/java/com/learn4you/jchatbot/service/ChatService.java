package com.learn4you.jchatbot.service;

import com.learn4you.jchatbot.dto.request.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
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