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
@Table(indexes = @Index(name = "idx_user", columnList = "user_id"))
public class ChatRoomUsers extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @OneToOne
    @JoinColumn(name = "last_chat_message_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ChatMessage lastChatMessage; //chatroomsuser로 이동해야함.

    public boolean isEmpty() {
        return this.lastChatMessage == null;
    }

}
