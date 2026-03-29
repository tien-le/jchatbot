package com.learn4you.jchatbot.controller;

import com.learn4you.jchatbot.dto.request.BillItem;
import com.learn4you.jchatbot.dto.request.ChatRequest;
import com.learn4you.jchatbot.dto.request.ExpenseInfo;
import com.learn4you.jchatbot.dto.request.FilmInfo;
import com.learn4you.jchatbot.service.ChatService;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatRequest chatRequest) {
        return chatService.chatStreamResponse(chatRequest);
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chat(request);
    }

    @PostMapping("/chat-with-image")
    public String chatWithImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("message") String message) {
        return chatService.chatWithImage(file, message);
    }

    @PostMapping("/chat-with-film-info")
    public List<FilmInfo> chatWithFilmInfo(@RequestBody ChatRequest request) {
        return chatService.chatWithFilmInfo(request);
    }

    /*Example:
        {
            "message": "View film in Pathe cimema 5 euros"
        }
    */
    @PostMapping("/chat-with-expense-info")
    public ExpenseInfo chatWithExpenseInfo(@RequestBody ChatRequest request) {
        return chatService.chatWithExpenseInfo(request);
    }

    @PostMapping("/chat-with-bill-info")
    public List<BillItem> chatWithBillInfo(
        @RequestParam("file") MultipartFile file,
        @RequestParam("message")  String message) {
        return chatService.chatWithBillInfo(file, message);
    }
}