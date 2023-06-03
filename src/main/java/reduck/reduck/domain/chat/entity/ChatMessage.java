package reduck.reduck.domain.chat.entity;

import lombok.*;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseEntity {
    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type;
    //채팅방 ID
    private String roomId;
    //보내는 사람
    private String sender;
    //내용
    private String message;
}