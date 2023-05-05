package reduck.reduck.domain.user.dto;

import lombok.*;
import reduck.reduck.domain.post.dto.PostOfUserResponseDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.UserProfileImg;

import java.util.List;


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
    private List<PostOfUserResponseDto> posts;

}
