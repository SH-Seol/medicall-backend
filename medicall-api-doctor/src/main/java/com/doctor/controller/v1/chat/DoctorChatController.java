package com.doctor.controller.v1.chat;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.chat.dto.ChatSummaryResponse;
import com.doctor.facade.DoctorChatFacade;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.chat.dto.ChatSummaryResult;

@RestController
@RequestMapping("api/v1/doctor/chat")
public class DoctorChatController {

    private final DoctorChatFacade doctorChatFacade;

    public DoctorChatController(DoctorChatFacade doctorChatFacade) {
        this.doctorChatFacade = doctorChatFacade;
    }

    @GetMapping("/{chatRoomId}/summary")
    public ChatSummaryResponse getSummary(@PathVariable("chatRoomId") Long chatRoomId,
                                          @Parameter(hidden = true)CurrentUser user) {
        ChatSummaryResult result = doctorChatFacade.summarizeChat(user, chatRoomId);
        return ChatSummaryResponse.from(result);
    }
}
