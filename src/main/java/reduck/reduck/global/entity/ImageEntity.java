package reduck.reduck.global.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class ImageEntity extends BaseEntity{
    private String uploadeFiledName;
    private String storageFileName;
    private String extension;
    private Long size;
    private String path;
}
