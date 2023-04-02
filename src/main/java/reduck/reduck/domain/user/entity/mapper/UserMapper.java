package reduck.reduck.domain.user.entity.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User from(Object obj) {
        User user= null;
        if (obj instanceof SignUpDto) {
            SignUpDto signUpDto = (SignUpDto) obj;
            user = User.builder()
                    .userId(signUpDto.getUserId())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .name(signUpDto.getName())
                    .email(signUpDto.getEmail())
                    .profileImg(signUpDto.getProfileImg())
                    .company(signUpDto.getCompany())
                    .school(signUpDto.getSchool())
                    .developAnnual(DevelopAnnual.getAnnual(signUpDto.getDevelopAnnual()))
                    .build();
        }
        return user;
    }
}
