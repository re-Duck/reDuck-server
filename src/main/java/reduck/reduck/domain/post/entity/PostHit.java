package reduck.reduck.domain.post.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostHit extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false, columnDefinition = "integer default 1")
    private int hits;
}
