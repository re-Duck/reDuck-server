package reduck.reduck.domain.rank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.rank.entity.Rank;
import reduck.reduck.domain.rank.repository.RankRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.exception.exception.NotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;

    @Transactional
    public void updateRank(User user, int xp) {
        Rank rank = rankRepository.findByUser(user).orElseThrow(() -> new NotFoundException());
        rankRepository.updateScore(rank, xp);
    }
}
