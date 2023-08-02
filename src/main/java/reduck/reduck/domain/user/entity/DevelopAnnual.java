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
    tenOver("10+"); //10년 이상
    private String numericalAnnual;

    public String getNumericalAnnual(){
        return numericalAnnual;
    }

    public String getLiteralAnnual(){
        return name();
    }
    public static String getNumericAnnual(String year) {
        DevelopAnnual developAnnual = Arrays.stream(values()).filter(value -> value.numericalAnnual.equals(year)).findAny().orElseThrow(IllegalArgumentException::new);
        return developAnnual.numericalAnnual;
    }

    public static String getLiteralAnnual(String year) {
        DevelopAnnual developAnnual = Arrays.stream(values())
                .filter(value -> value.numericalAnnual.equals(year))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        return developAnnual.name();
    }

    DevelopAnnual(String numericalAnnual) {
        this.numericalAnnual = numericalAnnual;
    }

    @Override
    public String toString() {
        return "개발연차 : " + this.numericalAnnual + "연차";
    }
}

