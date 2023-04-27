package reduck.reduck.domain.post.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import reduck.reduck.global.entity.ImageEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PostImage extends ImageEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

}
