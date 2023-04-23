package reduck.reduck.domain.user.entity.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.User;

public class UserMapper {

    public static User from(SignUpDto obj) {
        return User.builder()
                .userId(obj.getUserId())
                .password(obj.getPassword())
                .name(obj.getName())
                .email(obj.getEmail())
                .company(obj.getCompany())
                .companyEmail(obj.getCompanyEmail())
                .school(obj.getSchool())
                .schoolEmail(obj.getSchoolEmail())
                .developAnnual(DevelopAnnual.getAnnual(obj.getDevelopAnnual()))
                .build();
    }


}
