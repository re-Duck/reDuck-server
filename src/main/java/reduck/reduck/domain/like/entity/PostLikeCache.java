package reduck.reduck.domain.like.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeCache extends BaseEntity {

    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    private Post post;
}
