package reduck.reduck.domain.user.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_memberId", columnList = "memberId", unique = true))
public class Member extends BaseEntity {

    @Column(length = 20)
    String password;
    @Column(length = 20, unique = true)
    String memberId;
    @Column(length = 20)
    String name;
}
