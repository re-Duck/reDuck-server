package reduck.reduck.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.post.entity.QComment;
import reduck.reduck.domain.post.entity.QPost;
import reduck.reduck.domain.user.entity.QUser;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostDslRepositoryImpl implements PostDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Post> findByPostOriginId(String postOriginId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(QPost.post)
                .join(QPost.post.user, QUser.user)
                .leftJoin(QPost.post.comments, QComment.comment)
                .where(QPost.post.postOriginId.eq(postOriginId))
                .fetchOne());
    }

    @Override
    public Optional<List<Post>> findAllByPostTypeOrderByIdDescLimitPage(List<PostType> postType, Pageable pageable) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Post>> findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(List<PostType> postType, String postOriginId, Pageable pageable) {
        return Optional.empty();
    }

}
