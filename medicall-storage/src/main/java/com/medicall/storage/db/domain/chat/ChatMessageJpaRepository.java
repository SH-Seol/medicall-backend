package com.medicall.storage.db.domain.chat;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByChatRoomEntityIdAndIdLessThanOrderByIdDesc(Long chatRoomId, Long cursorId, Pageable pageable);
    List<ChatMessageEntity> findByChatRoomEntityIdOrderByIdDesc(Long chatRoomId, Pageable pageable);
}
