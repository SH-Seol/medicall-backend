package com.medicall.domain.chat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.medicall.domain.chat.dto.ChatCommand;
import com.medicall.domain.common.enums.SenderType;
import com.medicall.domain.chat.dto.ChatCommand.Message;
import com.medicall.domain.chat.dto.ChatResult;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Service
public class ChatSummaryService {

    private final WebClient webClient;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatValidator chatValidator;

    @Value("${openai.model}")
    private String apiModel;

    @Value("${openai.temperature}")
    private double temperature;

    public ChatSummaryService(ChatMessageRepository chatMessageRepository,
                              WebClient webClient,
                              ChatValidator chatValidator) {
        this.chatMessageRepository = chatMessageRepository;
        this.webClient = webClient;
        this.chatValidator = chatValidator;
    }



    /**
     * 채팅 내용 요약.
     * 방 번호만으로 조회하면 다른 사람의 상담 내용까지 열람할 수 있으므로
     * 요청자가 해당 채팅방의 참여자인지 먼저 검증한다.
     */
    public String summarizeChat(Long roomId, Long userId, SenderType senderType){
        chatValidator.validateChatRoomAccess(roomId, userId, senderType);

        List<ChatMessage> messages = getChatMessages(roomId);
        if(messages.isEmpty()){
            return "오늘 요약할 대화 내용이 없습니다.";
        }
        String chats = formatChats(messages);

        return callOpenAiSummary(chats);
    }

    /**
     * 요약 대상은 오늘 오간 대화로 한정한다.
     * 진료는 그날 단위로 이뤄지므로 지난 상담까지 섞이면 요약이 흐려지고,
     * 방이 오래될수록 프롬프트가 길어져 호출 비용과 입력 한도 초과 위험도 같이 커진다.
     */
    @Transactional(readOnly = true)
    protected List<ChatMessage> getChatMessages(Long roomId) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();

        return chatMessageRepository.findByChatRoomIdSince(roomId, startOfToday);
    }

    private String formatChats(List<ChatMessage> chatMessages) {
        StringBuilder chats = new StringBuilder();

        for (ChatMessage chatMessage : chatMessages) {
            String role = chatMessage.senderType()
                    .name().equals("DOCTOR") ? "D" : "P";
            chats.append(String.format("[%s]: %s\n", role, chatMessage.content()));
        }

        return chats.toString();
    }

    private String callOpenAiSummary(String formattedChats){
        String systemPrompt = "의료 요약기. 출력은 반드시 다음 JSON 형식만 허용: {\"symptom\":\"\", \"summary\":\"\"}";

        ChatCommand command = new ChatCommand(
                apiModel,
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", formattedChats)
                ),
                temperature,
                150
        );

        try{
            ChatResult result = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(command)
                    .retrieve()
                    .bodyToMono(ChatResult.class)
                    .block();

            if(result == null || result.choices().isEmpty()){
                throw new CoreException(CoreErrorType.OPENAI_NO_RESPONSE);
            }

            return result.choices().get(0).message()
                    .content();

        }catch (Exception e){
            throw new CoreException(CoreErrorType.OPENAI_NOT_CALLABLE, e);
        }
    }
}
