package reduck.reduck.domain.user.dto.mapper;

import reduck.reduck.domain.post.dto.mapper.PostOfUserResponseDtoMapper;
import reduck.reduck.domain.user.dto.UserInfoDtoRes;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.util.DevelopAnnualCalculation;

import java.time.LocalDateTime;

public class UserInfoDtoResMapper {
    public static UserInfoDtoRes from(User user) {
        String developAnnual = DevelopAnnualCalculation.calculate(user.getDevelopYear());
        UserInfoDtoRes userInfoDtoRes = UserInfoDtoRes.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .company(user.getCompany())
                .companyEmail(user.getCompanyEmail())
                .companyEmailAuthentication(user.isCompanyEmailAuthentication())
                .school(user.getSchool())
                .schoolEmail(user.getSchoolEmail())
                .schoolEmailAuthentication(user.isSchoolEmailAuthentication())
                .developAnnual(developAnnual)
                .userProfileImg(user.getProfileImg())
                .posts(PostOfUserResponseDtoMapper.from(user.getPosts()))
                .build();
        return userInfoDtoRes;
    }
}
