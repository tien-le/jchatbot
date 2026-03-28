package com.learn4you.jchatbot.service;

import com.learn4you.jchatbot.dto.request.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(ChatRequest request) {
        System.out.println("input message: " + request.message());

        SystemMessage systemMessage = new SystemMessage("""
                You are an expert assistant about AI Engineer, and your name is LAVIE.
                Your response should always be a formal voice.
                """);
        UserMessage userMessage = new UserMessage(request.message());
        Prompt prompt = new Prompt(systemMessage, userMessage);

//        return chatClient.prompt(request.message()).call().content();

        // https://docs.spring.io/spring-ai/reference/api/chatclient.html
//        return this.chatClient
//                       .prompt()
//                       .user(request.message())
//                       .call()
//                       .content();

        return this.chatClient
                       .prompt(prompt)
                       .call()
                       .content();
    }

    public String chatWithImage(MultipartFile file, String message) {
        System.out.println("input message: " + message);
        SystemMessage systemMessage = new SystemMessage("""
                You are an expert assistant about AI Engineer, and your name is LAVIE.
                Your response should always be a formal voice.
                """);

        /*
        * `Media.builder()` → starts building a `Media` object using the builder pattern.
        * `.mimeType(...)` → sets the file’s MIME type (e.g., `"image/png"`).
        * `.data(...)` → sets the file’s content as a `Resource`.
        * `.build()` → creates the actual `Media` object with the given properties.

        ✅ In short: it converts an uploaded file into a `Media` object with its content and type, ready to use.
         */
        Media media = Media
                              .builder()
                              .mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                              .data(file.getResource())
                              .build();
        ChatOptions chatOptions = ChatOptions
                                          .builder()
                                          .temperature(0.9D)
                                          .maxTokens(100)
                                          .build();
        /*
        ```java
        user(x -> x.media(media).text(message))
        ```
        Here:
        * `x` = the object that lets you **set text, attach media, etc.**
        * `x` is just a name for the “object representing the user’s message” inside the lambda --> a temporary variable.
        * `.media(media)` and `.text(message)` are **methods of that object**.
         */
        return this.chatClient
                       .prompt()
                       .options(chatOptions)
                       .system(systemMessage.getText())
                       .user(u -> u.media(media).text(message))
                       .call()
                       .content();
    }


}