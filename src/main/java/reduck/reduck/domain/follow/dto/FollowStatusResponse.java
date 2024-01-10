package reduck.reduck.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowStatusResponse {
    private Boolean isFollowing;

    private Boolean isFollower;
}
