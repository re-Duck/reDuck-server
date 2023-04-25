package reduck.reduck.domain.user.dto;

import lombok.*;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.UserProfileImg;


@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDtoRes {

    private String userId;
    private String name;
    private String email;
    private String company;
    private String companyEmail;
    private boolean companyEmailAuthentication;
    private String school;
    private String schoolEmail;
    private boolean schoolEmailAuthentication;
    private String developAnnual;
    private UserProfileImg userProfileImg;

}
