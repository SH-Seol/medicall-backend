package com.medicall.chat.controller.v1.chat;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.chat.controller.v1.chat.dto.ChatMessageListRequest;
import com.medicall.chat.controller.v1.chat.dto.SendChatMessageRequest;
import com.medicall.chat.facade.ChatFacade;
import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.chat.ChatMessage;
import com.medicall.domain.chat.ChatRoom;
import com.medicall.support.CursorPageResult;

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

    @GetMapping("/{chatRoomId}")
    public CursorPageResponse<ChatMessage> getChatMessages(@PathVariable Long chatRoomId,
                                              @Valid ChatMessageListRequest request,
                                              @Parameter(hidden = true) CurrentUser currentUser){
        CursorPageResult<ChatMessage> cursorPageResult = chatFacade.getChatMessages(chatRoomId, request, currentUser);
        List<ChatMessage> chatMessages = cursorPageResult.data().stream()
                .toList();
        return CursorPageResponse.of(chatMessages, request.cursorId(), cursorPageResult.nextCursorId());
    }

    @GetMapping
    public ResponseEntity<List<ChatRoom>> getChatRooms(@Parameter(hidden = true) CurrentUser currentUser){
        return null;
    }
}
