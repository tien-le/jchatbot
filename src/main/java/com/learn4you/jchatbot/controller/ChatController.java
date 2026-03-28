package com.learn4you.jchatbot.controller;

import com.learn4you.jchatbot.dto.request.ChatRequest;
import com.learn4you.jchatbot.dto.request.FilmInfo;
import com.learn4you.jchatbot.service.ChatService;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
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
}