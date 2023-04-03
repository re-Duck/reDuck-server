package reduck.reduck.domain.user.entity;

import lombok.*;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserProfileImg extends BaseEntity {
    private String uploadedName;
    private String storageName;
    private String path;
}
