package reduck.reduck.domain.user.entity;

import java.util.Arrays;

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
    private String annual;

    public static DevelopAnnual getAnnual(String year) {
        DevelopAnnual developAnnual = Arrays.stream(values()).filter(value -> value.annual.equals(year)).findAny().orElseThrow(IllegalArgumentException::new);
        return developAnnual;
    }
    DevelopAnnual(String annual) {
        this.annual = annual;
    }
}
