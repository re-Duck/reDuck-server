package reduck.reduck.domain.user.entity.mapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.User;

public class UserMapper {
    static PasswordEncoder passwordEncoder;

    public static User from(SignUpDto obj) {

        System.out.println("passwordEncoder = " + passwordEncoder);
        User user = User.builder()
                .userId(obj.getUserId())
                .password(obj.getPassword())
                .name(obj.getName())
                .email(obj.getEmail())
//                    .profileImg(signUpDto.getProfileImg())
                .company(obj.getCompany())
                .school(obj.getSchool())
                .developAnnual(DevelopAnnual.getAnnual(obj.getDevelopAnnual()))
                .build();

        return user;
    }


}
