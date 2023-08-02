package reduck.reduck.domain.user.entity.mapper;

import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.User;

public class UserMapper {

    public static User from(SignUpDto obj) {
        return User.builder()
                .userId(obj.getUserId())
                .password(obj.getPassword())
                .name(obj.getName())
                .email(obj.getEmail())
                .company(obj.getCompany())
                .school(obj.getSchool())
                .developYear(obj.getDevelopYear())
                .emailAuthentication(true)
                .build();
    }


}
