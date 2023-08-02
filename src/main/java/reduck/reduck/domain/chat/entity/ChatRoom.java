package reduck.reduck.domain.chat.entity;

import lombok.*;

import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatRoom extends BaseEntity {
    private String roomId;

//    @OneToOne
//    @JoinColumn(name = "chat_message_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
//    private ChatMessage lastChatMessage;

    private String lastChatMessage;

}
