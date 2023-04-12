package reduck.reduck.domain.post.entity;

import lombok.*;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(unique = true)
    private String postOriginId;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(columnDefinition = ("boolean default false"))
    private Boolean temporary;

}
