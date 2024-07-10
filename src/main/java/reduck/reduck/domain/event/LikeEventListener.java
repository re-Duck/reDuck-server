package reduck.reduck.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.rank.entity.Active;
import reduck.reduck.domain.rank.service.RankService;

@Component
@RequiredArgsConstructor
public class LikeEventListener {
    private static final int LIKE_TARGET_XP = Active.LIKE.getTargetXp();
    private static final int LIKE_USER_XP = Active.LIKE.getTargetXp();

    private final RankService rankService;

    /**
     * 게시글 작성자와, 본인의 금주 랭킹 점수를 업데이트한다.
     */
    @Async
    @EventListener
    public void onLikeEventHandler(LikeEvent event) {
        rankService.updateRank(event.getTargetUserId(),LIKE_TARGET_XP);
        rankService.updateRank(event.getActorId(),LIKE_USER_XP);
    }
}