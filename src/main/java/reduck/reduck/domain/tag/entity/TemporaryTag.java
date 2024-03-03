package reduck.reduck.domain.tag.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reduck.reduck.domain.post.entity.TemporaryPost;
import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryTag extends BaseEntity {

    private String title;

    @ManyToOne
    TemporaryPost temporaryPost;
}
