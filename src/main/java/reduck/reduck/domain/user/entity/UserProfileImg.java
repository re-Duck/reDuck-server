package reduck.reduck.domain.user.entity;

import lombok.*;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserProfileImg extends BaseEntity {
    private String uploadeFiledName;
    private String storageFileName;
    private String extension;
    private Long size;
    private String path;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}

