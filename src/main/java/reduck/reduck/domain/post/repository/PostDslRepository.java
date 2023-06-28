package reduck.reduck.domain.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;

import java.util.List;
import java.util.Optional;

public interface PostDslRepository {
    Optional<Post> findByPostOriginId(String postOriginId);

    Optional<List<Post>> findAllByPostTypeOrderByIdDescLimitPage(List<PostType> postType, Pageable pageable);

    Optional<List<Post>> findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(List<PostType> postType,
                                                                                String postOriginId,
                                                                                Pageable pageable);
}
