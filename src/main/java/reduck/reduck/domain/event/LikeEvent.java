package reduck.reduck.domain.event;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikeEvent {
    private Long targetUserId;
    private Long actorId;

    public static LikeEvent from(Long targetId, Long actorId){
        return LikeEvent.builder()
                .targetUserId(targetId)
                .actorId(actorId)
                .build();
    }
}
