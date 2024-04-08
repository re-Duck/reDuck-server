package reduck.reduck.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reduck.reduck.domain.board.entity.Comment;
import reduck.reduck.domain.like.entity.CommentLikeCache;

public interface CommentLikeCacheRepository extends JpaRepository<CommentLikeCache, Long> {
    @Modifying
    @Query("update CommentLikeCache clc set clc.count = clc.count + :afterCount where clc.comment = :comment")
    void updateLikeCount(@Param("afterCount") int afterCount, @Param("comment") Comment comment);

    void deleteByComment(Comment comment);
}
