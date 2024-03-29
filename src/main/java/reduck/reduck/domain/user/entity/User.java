package reduck.reduck.domain.user.entity;


import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.user.dto.ModifyUserDto;
import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(indexes = @Index(name = "idx_userId", columnList = "userId", unique = true))
@Where(clause = "del_yn=false")
@SQLDelete(sql = "UPDATE user SET del_yn=true, user_id=null where id=?")
public class User extends BaseEntity {

    private String password;
    @Column(length = 20, unique = true)
    private String userId;
    @Column(length = 20)
    private String name;

    private String email;
    @Getter
    private boolean emailAuthentication;

    private String company;
    @Column(columnDefinition = "varchar(255) default ''")
    private String companyEmail;

    @Column(columnDefinition = ("boolean default false"))
    private boolean companyEmailAuthentication;

    private String school;
    @Column(columnDefinition = "varchar(255) default ''")
    private String schoolEmail;
    @Column(columnDefinition = ("boolean default false"))
    private boolean schoolEmailAuthentication;

    private int developYear;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    @Embedded
    private UserProfileImg profileImg;

    @Column(columnDefinition = "boolean default false")
    private boolean delYn;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user",  cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setUser(this));
    }

    public String getProfileImgPath() {
        return this.profileImg != null ? this.profileImg.getPath() : "";
    }
    public void updateProfileImg(UserProfileImg userProfileImg) {
        this.profileImg = userProfileImg;
    }
    public void updateFrom(ModifyUserDto modifyUserDto) {
        this.password = modifyUserDto.getPassword();
        this.name = modifyUserDto.getName();
        this.email = modifyUserDto.getEmail();
        this.company = modifyUserDto.getCompany();
        this.companyEmail = modifyUserDto.getCompanyEmail();
        this.school = modifyUserDto.getSchool();
        this.schoolEmail = modifyUserDto.getSchoolEmail();
        this.developYear = modifyUserDto.getDevelopYear();
    }
    public void authenticateCompanyEmail() {
        this.companyEmailAuthentication = true;
    }
    public void authenticateSchoolEmail() {
        this.schoolEmailAuthentication = true;
    }
}
