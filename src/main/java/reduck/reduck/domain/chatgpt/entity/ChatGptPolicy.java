package reduck.reduck.domain.chatgpt.entity;

public enum ChatGptPolicy {
    STANDARD(10), ULTIMATE(100),
    ;
    private int limitUsage;

    ChatGptPolicy(int limitUsage) {
        this.limitUsage = limitUsage;
    }
    public int getLimitUsage() {
        return this.limitUsage;
    }

}
