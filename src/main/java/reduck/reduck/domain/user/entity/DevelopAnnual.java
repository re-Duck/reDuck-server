package reduck.reduck.domain.user.entity;

import java.util.Arrays;

public enum DevelopAnnual {
    zero("0","zero"), // 취준생
    one("1","one"),
    two("2","two"),
    three("3","three"),
    four("4","four"),
    five("5","five"),
    six("6","six"),
    seven("7","seven"),
    eight("8","eight"),
    nine("9","nine"),
    tenOver("10+","tenOver"); //10년 이상
    private String numericalAnnual;
    private String literalAnnual;
    public String getNumericalAnnual(){
        return numericalAnnual;
    }

    public String getLiteralAnnual(){
        return literalAnnual;
    }
    public static String getNumericAnnual(String year) {
        DevelopAnnual developAnnual = Arrays.stream(values()).filter(value -> value.numericalAnnual.equals(year)).findAny().orElseThrow(IllegalArgumentException::new);
        return developAnnual.numericalAnnual;
    }

    public static String getLiteralAnnual(String year) {
        DevelopAnnual developAnnual = Arrays.stream(values()).filter(value -> value.numericalAnnual.equals(year)).findAny().orElseThrow(IllegalArgumentException::new);
        return developAnnual.literalAnnual;
    }

    DevelopAnnual(String numericalAnnual, String literalAnnual) {
        this.numericalAnnual = numericalAnnual;
        this.literalAnnual = literalAnnual;
    }
}
