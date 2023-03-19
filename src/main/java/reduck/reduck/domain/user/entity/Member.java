package reduck.reduck.domain.user.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(length = 20)
    String password;
    @Column(length = 20)
    String memberId;

}
