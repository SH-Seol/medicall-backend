package com.medicall.storage.db.domain.chat;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.medicall.domain.chat.ChatMessage;
import com.medicall.domain.chat.ChatMessageRepository;

@Repository
public class ChatMessageCoreRepository implements ChatMessageRepository {

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;

    public ChatMessageCoreRepository(ChatMessageJpaRepository jpaRepository,
                                     ChatRoomJpaRepository chatRoomJpaRepository) {
        this.chatMessageJpaRepository = jpaRepository;
        this.chatRoomJpaRepository = chatRoomJpaRepository;
    }


    public void save(ChatMessage message){
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.getReferenceById(message.chatRoomId());
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity(
                chatRoomEntity,
                message.senderType(),
                message.senderId(),
                message.content(),
                false
        );
        chatMessageJpaRepository.save(chatMessageEntity);
    }
    public List<ChatMessage> findByChatRoomId(Long chatRoomId, Long cursorId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);

        List<ChatMessageEntity> entities;

        if (cursorId == null) {
            entities = chatMessageJpaRepository.findByChatRoomEntityIdOrderByIdDesc(chatRoomId, pageRequest);
        } else {
            entities = chatMessageJpaRepository.findByChatRoomEntityIdAndIdLessThanOrderByIdDesc(chatRoomId, cursorId, pageRequest);
        }

        return entities.stream()
                .map(ChatMessageEntity::toDomainModel)
                .toList();
    }
}
