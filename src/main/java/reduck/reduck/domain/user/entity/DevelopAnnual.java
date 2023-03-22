package reduck.reduck.domain.user.entity;

public enum DevelopAnnual {
    zero("0"), // 취준생
    one("1"),
    two("2"),
    three("3"),
    four("4"),
    five("5"),
    six("6"),
    seven("7"),
    eight("8"),
    nine("9"),
    tenOver("10~"); //10년 이상
    String annual;

    DevelopAnnual(String annual) {
        this.annual = annual;
    }
}
