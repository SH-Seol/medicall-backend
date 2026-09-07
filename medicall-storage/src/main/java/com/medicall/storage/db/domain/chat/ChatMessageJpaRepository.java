package com.medicall.storage.db.domain.chat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.medicall.domain.common.enums.SenderType;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByChatRoomEntityIdAndIdLessThanOrderByIdDesc(Long chatRoomId, Long cursorId, Pageable pageable);
    List<ChatMessageEntity> findByChatRoomEntityIdOrderByIdDesc(Long chatRoomId, Pageable pageable);
    List<ChatMessageEntity> findAllByChatRoomEntityIdOrderByCreatedAtAsc(Long chatRoomId);

    List<ChatMessageEntity> findAllByChatRoomEntityIdAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(
            Long chatRoomId, LocalDateTime since);

    /**
     * 채팅방별 마지막 메시지 (목록 화면에서 방 개수만큼 조회하지 않도록 한 번에 가져온다)
     */
    @Query("""
            SELECT m FROM ChatMessageEntity m
            WHERE m.chatRoomEntity.id IN :chatRoomIds
              AND m.id IN (SELECT MAX(sub.id) FROM ChatMessageEntity sub
                           WHERE sub.chatRoomEntity.id IN :chatRoomIds
                           GROUP BY sub.chatRoomEntity.id)
            """)
    List<ChatMessageEntity> findLastMessages(@Param("chatRoomIds") List<Long> chatRoomIds);

    /**
     * 채팅방별 내가 읽지 않은(상대가 보낸) 메시지 수
     */
    @Query("""
            SELECT m.chatRoomEntity.id, COUNT(m)
            FROM ChatMessageEntity m
            WHERE m.chatRoomEntity.id IN :chatRoomIds
              AND m.senderType <> :myType
              AND m.isRead = false
            GROUP BY m.chatRoomEntity.id
            """)
    List<Object[]> countUnreadByChatRoomIds(@Param("chatRoomIds") List<Long> chatRoomIds,
                                            @Param("myType") SenderType myType);

    /**
     * 상대가 보낸 메시지를 읽음 처리한다.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE ChatMessageEntity m SET m.isRead = true
            WHERE m.chatRoomEntity.id = :chatRoomId
              AND m.senderType <> :myType
              AND m.isRead = false
            """)
    int markAsRead(@Param("chatRoomId") Long chatRoomId, @Param("myType") SenderType myType);
}
