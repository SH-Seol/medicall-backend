package com.medicall.storage.db.core.chat;

import com.medicall.storage.db.core.common.domain.BaseEntity;
import com.medicall.storage.db.core.common.enums.SenderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ChatMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false, length = 1000)
    private String content;

    private boolean isRead;

    protected ChatMessage() {}

    public ChatMessage(ChatRoom chatRoom, SenderType senderType, Long senderId, String content, boolean isRead) {
        this.chatRoom = chatRoom;
        this.senderType = senderType;
        this.senderId = senderId;
        this.content = content;
        this.isRead = isRead;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
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
}
