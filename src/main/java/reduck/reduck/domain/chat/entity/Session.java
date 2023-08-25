package reduck.reduck.domain.chat.entity;

import lombok.*;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Session extends BaseEntity {

    private String sessionId;

    private String userId;

    private String roomId;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    public static Session init(String sessionId, String userId, String roomId) {
        return Session.builder()
                .sessionId(sessionId)
                .userId(userId)
                .roomId(roomId)
                .status(SessionStatus.ON)
                .build();
    }

    @Override
    public String toString() {
        return "{sessionId : " + this.sessionId +
                "\nuserId : " + this.userId +
                "\nroomId : " + this.roomId +
                "\nstatus : " + this.status +"}";
    }

    public void off() {
        this.status = SessionStatus.OFF;
    }

    public void on() {
        this.status = SessionStatus.ON;
    }
}

