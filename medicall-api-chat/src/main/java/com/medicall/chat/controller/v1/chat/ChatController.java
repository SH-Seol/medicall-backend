package com.medicall.chat.controller.v1.chat;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.chat.controller.v1.chat.dto.SendChatMessageRequest;
import com.medicall.chat.facade.ChatFacade;
import com.medicall.common.support.CurrentUser;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatFacade chatFacade;

    public ChatController(ChatFacade chatFacade) {
        this.chatFacade = chatFacade;
    }

    @PostMapping("/messages")
    public ResponseEntity<Void> sendMessage(
            @RequestParam Long appointmentId,
            @RequestBody SendChatMessageRequest request, @Parameter(hidden = true) CurrentUser currentUser
    ) {
        chatFacade.sendMessage(
                currentUser,
                appointmentId,
                request.content()
        );

        return ResponseEntity.ok().build();
    }
}
