package reduck.reduck.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reduck.reduck.domain.user.entity.User;

@Getter
@Setter
@Builder
public class FollowerResponse {
    private String userId;
    private String userName;
    private String profileImg;

    public static FollowerResponse from(User user){
        return FollowerResponse.builder()
                .userId(user.getUserId())
                .userName(user.getName())
                .profileImg(user.getProfileImgPath())
                .build();
    }
}
