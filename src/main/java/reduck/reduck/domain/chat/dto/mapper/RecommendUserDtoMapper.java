package reduck.reduck.domain.chat.dto.mapper;

import reduck.reduck.domain.chat.dto.RecommendUserDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.util.DevelopAnnualCalculation;

public class RecommendUserDtoMapper {

    public static RecommendUserDto from(User user) {
        return RecommendUserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .company(user.getCompany())
                .companyEmailAuthentication(user.isCompanyEmailAuthentication())
                .developAnnual(DevelopAnnualCalculation.calculate(user.getDevelopYear()))
                .userProfileImagePath(user.getProfileImgPath())
                .build();
    }
}
