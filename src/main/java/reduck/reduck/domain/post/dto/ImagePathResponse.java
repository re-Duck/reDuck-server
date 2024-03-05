package reduck.reduck.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImagePathResponse {
    @Schema(description = "이미지 저장 경로", defaultValue = "*/reduck/my-name-is-reduck.jpg")
    private String imagePath;
}
