package reduck.reduck.domain.post.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

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
