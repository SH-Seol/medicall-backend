package com.medicall.storage.db.domain.chat;

import com.medicall.domain.chat.ChatMessage;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.domain.common.enums.SenderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoomEntity;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false, length = 1000)
    private String content;

    private boolean isRead;

    protected ChatMessageEntity() {}

    public ChatMessageEntity(ChatRoomEntity chatRoomEntity, SenderType senderType, Long senderId, String content, boolean isRead) {
        this.chatRoomEntity = chatRoomEntity;
        this.senderType = senderType;
        this.senderId = senderId;
        this.content = content;
        this.isRead = isRead;
    }

    public ChatRoomEntity getChatRoom() {
        return chatRoomEntity;
    }

    public SenderType getSenderType() {
        return senderType;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    public ChatMessage toDomainModel() {
        return new ChatMessage(
                this.id,
                chatRoomEntity.getId(),
                senderType,
                senderId,
                content,
                isRead,
                getCreatedAt()
        );
    }
}
