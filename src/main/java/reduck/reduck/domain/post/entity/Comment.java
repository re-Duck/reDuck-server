package reduck.reduck.domain.post.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import reduck.reduck.domain.post.dto.UpdateCommentDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
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

    @Column(columnDefinition = "varchar(255) default 'root'")
    private String parentCommentOriginId;

    public void updateFrom(UpdateCommentDto commentDto) {
        this.commentContent = commentDto.getContent();
    }
}
