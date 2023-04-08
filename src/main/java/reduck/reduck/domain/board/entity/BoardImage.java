package reduck.reduck.domain.board.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import reduck.reduck.global.entity.ImageEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardImage extends ImageEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

}
