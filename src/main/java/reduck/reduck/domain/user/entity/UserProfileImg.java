package reduck.reduck.domain.user.entity;

import lombok.*;

import jakarta.persistence.*;


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
@Data
public class UserProfileImg {
    private String uploadedFileName;
    private String storagedFileName;
    private String extension;
    private Long size;
    private String path;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(referencedColumnName = "userId")
//    private User user;
}

