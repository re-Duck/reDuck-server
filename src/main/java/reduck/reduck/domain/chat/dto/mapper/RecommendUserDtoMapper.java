package reduck.reduck.domain.chat.dto.mapper;

import reduck.reduck.domain.chat.dto.RecommendUserDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.util.DevelopAnnualCalculation;

public class RecommendUserDtoMapper {

    public static RecommendUserDto from(User user) {
        return RecommendUserDto.builder()
                .userId(user.getUserId())
                .company(user.getCompany())
                .companyAuthentication(user.isCompanyEmailAuthentication())
                .developYear(DevelopAnnualCalculation.calculate(user.getDevelopYear()))
                .profilePath(user.getProfileImgPath())
                .build();
    }
}
