package com.learn4you.jchatbot.service;

import com.learn4you.jchatbot.dto.request.BillItem;
import com.learn4you.jchatbot.dto.request.ExpenseInfo;
import com.learn4you.jchatbot.dto.request.ChatRequest;
import com.learn4you.jchatbot.dto.request.FilmInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;
    private final ChatMemory chatMemory;

    public ChatService(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        String systemMessage = """
                You are an expert assistant about AI Engineer.
                Your response should always be a formal voice.
                """;
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;
        this.chatMemory = MessageWindowChatMemory.builder()
                                    .chatMemoryRepository(jdbcChatMemoryRepository)
                                    .maxMessages(30)  // default=20
                                    .build();
        this.chatClient = builder
                              .defaultSystem(systemMessage)
                              .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                              .build();
    }

    public String chat(ChatRequest request) {
        System.out.println("-".repeat(32));
        System.out.println("input message: " + request.message());

        String conversationId = "Conversation-01";

        //BEFORE call
        var historyBefore = this.chatMemory.get(conversationId);
        System.out.println("=== MEMORY BEFORE ===");
        historyBefore.forEach(memory -> System.out.println(memory.getText()));

        UserMessage userMessage = new UserMessage(request.message());
        Prompt prompt = new Prompt(userMessage);

//        return chatClient.prompt(request.message()).call().content();

        // https://docs.spring.io/spring-ai/reference/api/chatclient.html
//        return this.chatClient
//                       .prompt()
//                       .user(request.message())
//                       .call()
//                       .content();
//        String conversationId = UUID.randomUUID().toString();

        String response = this.chatClient
                       .prompt(prompt)
                       .advisors(a->a.param(ChatMemory.CONVERSATION_ID, conversationId))
                       .call()
                       .content();

        // AFTER call
        System.out.println("-".repeat(32));
        var historyAfter = chatMemory.get(conversationId);
        System.out.println("=== MEMORY AFTER ===");
        historyAfter.forEach(msg -> System.out.println(msg.getText()));

        return response;
    }

    public List<FilmInfo> chatWithFilmInfo(ChatRequest request) {
        System.out.println("input message: " + request.message());

        UserMessage userMessage = new UserMessage(request.message());
        Prompt prompt = new Prompt(userMessage);
        try {
            return this.chatClient
                           .prompt(prompt)
                           .call()
                           .entity(new ParameterizedTypeReference<List<FilmInfo>>() {});
        } catch (Exception e) {
            System.out.println("Exception occurred. Because: " + e.getMessage());
            return null;
        }
    }

    public String chatWithImage(MultipartFile file, String message) {
        System.out.println("input message: " + message);

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
                       .user(u -> u.media(media).text(message))
                       .call()
                       .content();
    }

    public ExpenseInfo chatWithExpenseInfo(ChatRequest request) {
        System.out.println("input message: " + request.message());

        UserMessage userMessage = new UserMessage(request.message());
        Prompt prompt  = new Prompt(userMessage);
        return this.chatClient
                   .prompt(prompt)
                   .call()
                   .entity(ExpenseInfo.class);
    }

    public List<BillItem> chatWithBillInfo(MultipartFile file, String message) {
        System.out.println("input message: " + message);
        Media media = Media
                          .builder()
                          .mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                          .data(file.getResource())
                          .build();
        ChatOptions chatOptions = new ChatOptions() {
            @Override
            public String getModel() {
                return "gemini-2.5-flash-lite";
            }

            @Override
            public Double getFrequencyPenalty() {
                return 0.0;
            }

            @Override
            public Integer getMaxTokens() {
                return 100;
            }

            @Override
            public Double getPresencePenalty() {
                return 0.0;
            }

            @Override
            public List<String> getStopSequences() {
                return List.of();
            }

            @Override
            public Double getTemperature() {
                return 0.5;
            }

            @Override
            public Integer getTopK() {
                return 0;
            }

            @Override
            public Double getTopP() {
                return 0.0;
            }

            @Override
            public <T extends ChatOptions> T copy() {
                return null;
            }
        };
        return this.chatClient
                   .prompt()
                   .options(chatOptions)
                   .user(u -> u.media(media).text(message))
                   .call()
                   .entity(new ParameterizedTypeReference<List<BillItem>>() {
                   });
    }
}