package reduck.reduck.domain.user.entity;


import lombok.*;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(indexes = @Index(name = "idx_userId", columnList = "userId", unique = true))
public class User extends BaseEntity {

    @Column(length = 20)
    String password;
    @Column(length = 20, unique = true)
    String userId;
//    @Column(length = 20)
//    String name;
//
//    String email;
//    String profileImg;
//    String company;
//    String school;
//    String developAnnual;


}
