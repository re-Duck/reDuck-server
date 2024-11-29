package reduck.reduck.domain.rank.entity;

import lombok.*;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder @Getter
@Table(name = "rank_caches")
public class RankCache extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
