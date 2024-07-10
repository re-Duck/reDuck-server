package reduck.reduck.domain.rank.entity;

public enum Active {
    LIKE(100, 10),
    HIT(1, 0),
    SCRAP(500, 50);

    private int targetXp;
    private int userXp;

    Active(int targetXp, int userXp)
    {
        this.targetXp = targetXp;
        this.userXp = userXp;
    }

    public int getTargetXp() {
        return this.targetXp;
    }
    public int getUserXp() {
        return this.userXp;
    }
}
