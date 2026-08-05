package com.doctor.facade;

import org.springframework.stereotype.Component;

import com.medicall.common.support.CurrentUser;
import com.medicall.domain.chat.ChatSummaryService;
import com.medicall.domain.chat.dto.ChatSummaryResult;
import com.medicall.domain.common.enums.SenderType;

@Component
public class DoctorChatFacade {

    private final ChatSummaryService summaryService;

    public DoctorChatFacade(ChatSummaryService summaryService) {
        this.summaryService = summaryService;
    }

    public ChatSummaryResult summarizeChat(CurrentUser user, Long roomId) {
        String summarized = summaryService.summarizeChat(roomId, user.userId(), SenderType.DOCTOR);
        return new ChatSummaryResult(summarized);
    }
}
