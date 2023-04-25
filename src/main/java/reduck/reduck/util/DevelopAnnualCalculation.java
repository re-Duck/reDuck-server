package reduck.reduck.util;

import reduck.reduck.domain.user.entity.DevelopAnnual;

import java.time.LocalDateTime;

public class DevelopAnnualCalculation {
    public static String calculate(int developYear) {
        int curYear = LocalDateTime.now().getYear();

        if (developYear == 0) { //예비 개발자
            return DevelopAnnual.zero.getNumericalAnnual();
        }
        int annual = curYear - developYear + 1;
        if (annual >= 10) { // 10년 이상 개발자.
            return DevelopAnnual.tenOver.getNumericalAnnual();
        }
        return DevelopAnnual.getNumericAnnual(String.valueOf(annual));
    }
}
