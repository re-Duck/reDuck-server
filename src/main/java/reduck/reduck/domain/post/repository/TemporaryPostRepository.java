package reduck.reduck.domain.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reduck.reduck.domain.post.entity.TemporaryPost;
import reduck.reduck.domain.user.entity.User;

import java.util.List;

public interface TemporaryPostRepository extends JpaRepository<TemporaryPost, Long> {
    @Query("Select tp from TemporaryPost tp join fetch tp.user where tp.user = :user and (select tp2 from TemporaryPost tp2 where tp2.postOriginId = :temporaryPostOriginId) > tp.id order by tp.id desc")
    List<TemporaryPost> findAllByUserAndPostOriginIdOrderByIdDescLimitPage(
            @Param("user") User user,
            @Param("temporaryPostOriginId") String temporaryPostOriginId,
            Pageable pageable);

    @Query("Select tp from TemporaryPost tp join fetch tp.user where tp.user = :user order by tp.id desc")
    List<TemporaryPost> findAllByUserOrderByIdDescLimitPage(
            @Param("user") User user,
            Pageable pageable);
}
