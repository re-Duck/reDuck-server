package reduck.reduck.domain.auth.dto;

import lombok.*;
import reduck.reduck.domain.user.entity.Authority;
import reduck.reduck.domain.user.entity.UserProfileImg;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDto {

    private String userId;

    private String name;

    private String email;

    private String userProfileImgPath="";
    private String accessToken;
    private String refreshToken;


}
