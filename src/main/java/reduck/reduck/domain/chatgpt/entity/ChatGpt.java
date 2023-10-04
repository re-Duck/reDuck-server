package reduck.reduck.domain.chatgpt.entity;


import lombok.Getter;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
public class ChatGpt extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private ChatGptMembership gptMembership;

    public boolean isUsable(Long usage) {
        return this.gptMembership.getLimitUsage() > usage;
    }

    public void upgradeMembership(ChatGptMembership membership) {
        this.gptMembership = membership;
    }
}
