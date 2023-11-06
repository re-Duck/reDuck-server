package reduck.reduck.domain.chat.entity;

import lombok.*;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_chat_room_id", columnList = "chat_room_id"),
        @Index(name = "idx_pk_chat_room_id", columnList = "chat_room_id, id")})
public class ChatMessage extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(unique = true)
    private String messageId;

    //채팅방 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ChatRoom room;

    //보내는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User sender;
    //내용
    private String message;


}