package reduck.reduck.domain.post.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryPost extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String postTitle;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(unique = true)
    private String postOriginId;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    public void updateFrom(PostDto dto) {
        this.postTitle = dto.getTitle();
        this.content = dto.getContent();
        this.postType = dto.getPostType();
    }
}
