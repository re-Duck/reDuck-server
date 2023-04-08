package reduck.reduck.global.entity;

import lombok.*;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity extends BaseEntity{
    private String uploadeFiledName;
    private String storageFileName;
    private String extension;
    private Long size;
    private String path;
}
