package reduck.reduck.domain.board.entity;

import lombok.*;
import reduck.reduck.global.entity.ImageEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardImage extends ImageEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

}
