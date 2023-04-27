package reduck.reduck.domain.auth.entity;

import lombok.*;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userPKId", referencedColumnName = "id")
    private User user;

    private String refreshToken;

}
