package reduck.reduck.domain.chat.dto.mapper;

import reduck.reduck.domain.chat.dto.RecommendUserResDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.util.DevelopAnnualCalculation;

public class RecommendUserResDtoMapper {

    public static RecommendUserResDto from(User user) {
        return RecommendUserResDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .company(user.getCompany())
                .companyEmailAuthentication(user.isCompanyEmailAuthentication())
                .developAnnual(DevelopAnnualCalculation.calculate(user.getDevelopYear()))
                .userProfileImgPath(user.getProfileImgPath())
                .build();
    }
}
