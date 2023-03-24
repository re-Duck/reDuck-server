package reduck.reduck.domain.user.entity;


import lombok.*;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(indexes = @Index(name = "idx_userId", columnList = "userId", unique = true))
public class User extends BaseEntity {

    @Column(length = 20)
    private String password;
    @Column(length = 20, unique = true)
    private String userId;
    @Column(length = 20)
    private String name;

    private    String email;
    private String profileImg;
    private String company;
    private String school;

    @Enumerated(EnumType.STRING)
    private DevelopAnnual developAnnual;


}
