package reduck.reduck.domain.chatgpt.entity;

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

}
