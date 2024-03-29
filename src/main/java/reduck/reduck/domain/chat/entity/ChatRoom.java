package reduck.reduck.domain.chat.entity;

import lombok.*;

import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.*;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(indexes = @Index(name = "idx_alias", columnList = "alias"))
public class ChatRoom extends BaseEntity {
    private String roomId;

    /**
     * format : "{userId1},{userId2},,,"
     */
    private String roomName;
    private String alias; // 1:1의 경우, 기존 채팅방의 존재 유무가 중요.


}
