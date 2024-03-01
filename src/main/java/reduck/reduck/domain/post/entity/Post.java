package reduck.reduck.domain.post.entity;

import lombok.*;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    private String postTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(unique = true)
    private String postOriginId;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(columnDefinition = ("boolean default false"))
    private Boolean temporary;

    @Builder.Default
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments= new ArrayList(); //양방향 매핑 순환참조 문제 발생.

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private PostHit postHit;


    public void setUser(User user) {
        this.user = user;
    }

    public void setPostHit(PostHit postHit) {
        this.postHit = postHit;
    }
    public void updateFrom(PostDto dto) {
        this.postTitle = dto.getTitle();
        this.content = dto.getContent();
        this.postType = dto.getPostType().toUpperCase();
    }
}
