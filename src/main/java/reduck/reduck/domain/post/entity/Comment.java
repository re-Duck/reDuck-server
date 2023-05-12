package reduck.reduck.domain.post.entity;

import lombok.*;
import reduck.reduck.domain.post.dto.CommentDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(unique = true)
    private String commentOriginId;

    @Column(columnDefinition = "boolean default false")
    private Boolean pin;

    @Column(columnDefinition = "integer default 0")
    private int likes;

    public void updateFrom(CommentDto commentDto) {
        this.commentContent = commentDto.getContent();

    }

}
