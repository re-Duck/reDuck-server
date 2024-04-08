package reduck.reduck.domain.like.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reduck.reduck.domain.board.entity.Comment;
import reduck.reduck.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeCache extends BaseEntity {

    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    private Comment comment;
}