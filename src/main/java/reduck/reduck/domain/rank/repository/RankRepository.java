package reduck.reduck.domain.rank.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.rank.entity.Rank;
import reduck.reduck.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {
    Optional<Rank> findByUser(User user);

    @Modifying
    @Query("update Rank r set r.score = r.score + :xp where r = :rank")
    void updateScore(@Param("rank") Rank rank, @Param("xp") long xp);
}
