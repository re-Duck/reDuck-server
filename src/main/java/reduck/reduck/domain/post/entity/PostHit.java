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
@AllArgsConstructor
@NoArgsConstructor
public class PostHit extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Post post;

    private int hits;
}
