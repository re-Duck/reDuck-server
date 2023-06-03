package reduck.reduck.domain.chat.entity;

import lombok.*;

import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatRoom extends BaseEntity {
    private String roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String roomName;

}
