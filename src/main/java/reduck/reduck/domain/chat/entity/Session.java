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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ChatRoom room;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    public static Session init(String sessionId, String userId, ChatRoom room) {
        return Session.builder()
                .sessionId(sessionId)
                .userId(userId)
                .room(room)
                .status(SessionStatus.ON)
                .build();
    }

    @Override
    public String toString() {
        return "{sessionId : " + this.sessionId +
                "\nuserId : " + this.userId +
                "\nroomId : " + this.room +
                "\nstatus : " + this.status + "}";
    }

    public void off() {
        this.status = SessionStatus.OFF;
    }

    public void on() {
        this.status = SessionStatus.ON;
    }

    public boolean isOn() {
        return this.status == SessionStatus.ON;
    }

    public void updateSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

