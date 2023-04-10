package reduck.reduck.domain.user.entity;


import lombok.*;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(indexes = @Index(name = "idx_userId", columnList = "userId", unique = true))
public class User extends BaseEntity {


    private String password;
    @Column(length = 20, unique = true)
    private String userId;
    @Column(length = 20)
    private String name;

    private String email;
    private String company;
    private String school;

    @Enumerated(EnumType.STRING)
    private DevelopAnnual developAnnual;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    @Embedded
    private UserProfileImg profileImg;

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setUser(this));
    }

    @Override
    public String toString() {

        return this.getPassword() + "\n" + this.profileImg.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
