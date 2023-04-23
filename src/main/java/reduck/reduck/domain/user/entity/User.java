package reduck.reduck.domain.user.entity;


import com.sun.istack.NotNull;
import lombok.*;
import reduck.reduck.domain.user.dto.ModifyUserDto;
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
    private String companyEmail;
    @Column(columnDefinition = ("boolean default false"))
    private boolean companyEmailAuthentication;

    private String school;
    private String schoolEmail;
    @Column(columnDefinition = ("boolean default false"))
    private boolean schoolEmailAuthentication;

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

    public void updateProfileImg(UserProfileImg userProfileImg) {
        this.profileImg = userProfileImg;
    }
    public void updateFrom(ModifyUserDto modifyUserDto) {
        this.name = modifyUserDto.getName();
        this.email = modifyUserDto.getEmail();
        this.company = modifyUserDto.getCompany();
        this.companyEmail = modifyUserDto.getCompanyEmail();
        this.school = modifyUserDto.getSchool();
        this.schoolEmail = modifyUserDto.getSchoolEmail();
        this.developAnnual = DevelopAnnual.getAnnual(modifyUserDto.getDevelopAnnual());
    }

    public void authenticatedCompanyEmail() {
        this.companyEmailAuthentication = true;
    }

    public void authenticatedSchoolEmail() {
        this.schoolEmailAuthentication = true;
    }
}
