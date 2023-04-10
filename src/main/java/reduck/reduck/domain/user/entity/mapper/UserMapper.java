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
//                    .profileImg(signUpDto.getProfileImg())
                .company(obj.getCompany())
                .school(obj.getSchool())
                .developAnnual(DevelopAnnual.getAnnual(obj.getDevelopAnnual()))
                .build();
    }


}
