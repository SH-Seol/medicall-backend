package com.medicall.domain.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.medicall.domain.chat.dto.ChatCommand;
import com.medicall.domain.chat.dto.ChatCommand.Message;
import com.medicall.domain.chat.dto.ChatResult;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Service
public class ChatSummaryService {

    private final WebClient webClient;
    private final ChatMessageRepository chatMessageRepository;

    @Value("${openai.model}")
    private String apiModel;

    @Value("${openai.temperature}")
    private double temperature;

    public ChatSummaryService(ChatMessageRepository chatMessageRepository,
                              WebClient webClient) {
        this.chatMessageRepository = chatMessageRepository;
        this.webClient = webClient;
    }



    public String summarizeChat(Long roomId){
        List<ChatMessage> messages = getChatMessages(roomId);
        String chats = formatChats(messages);

        return callOpenAiSummary(chats);
    }

    @Transactional(readOnly = true)
    protected List<ChatMessage> getChatMessages(Long roomId) {
        return chatMessageRepository.findAllByChatRoomId(roomId);
    }

    private String formatChats(List<ChatMessage> chatMessages) {
        StringBuilder chats = new StringBuilder();

        for (ChatMessage chatMessage : chatMessages) {
            String role = chatMessage.senderType()
                    .name().equals("DOCTOR") ? "의사" : "환자";
            chats.append(String.format("[%s]: %s\n", role, chatMessage.content()));
        }

        return chats.toString();
    }

    private String callOpenAiSummary(String formattedChats){
        String systemPrompt = """
                당신은 의료 상담 요약 전문가입니다.
                제공된 채팅 기록을 바탕으로 다음 항목을 요약하세요:
                - 환자 증상
                - 요약 (3줄 이내)
                """;

        ChatCommand command = new ChatCommand(
                apiModel,
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", formattedChats)
                ),
                temperature
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
