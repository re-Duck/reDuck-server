package reduck.reduck.domain.chatgpt.entity;

import reduck.reduck.domain.post.entity.PostType;

import java.util.Arrays;

public enum ChatGptMembership {
    STANDARD(10), ULTIMATE(100),
    ;
    private int limitUsage;

    ChatGptMembership(int limitUsage) {
        this.limitUsage = limitUsage;
    }
    public int getLimitUsage() {
        return this.limitUsage;
    }

    public static ChatGptMembership getMembership(ChatGptMembership membership) {

        return Arrays.stream(values())
                .filter(value -> membership.equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
