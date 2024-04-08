package reduck.reduck.domain.user.dto;

import lombok.*;
import reduck.reduck.domain.board.dto.PostOfUserResponseDto;

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
    private String userProfileImgPath;
    private List<PostOfUserResponseDto> posts;

}
