package reduck.reduck.domain.chatgpt.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatGpt extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private ChatGptMembership gptMembership;
    @Builder
    public ChatGpt(User user){
        this.user = user;
        this.gptMembership = ChatGptMembership.STANDARD;
    }

    public boolean isUsable(Long usage) {
        return this.gptMembership.getLimitUsage() > usage;
    }

    public void modifyMembership(ChatGptMembership membership) {
        this.gptMembership = membership;
    }
}
