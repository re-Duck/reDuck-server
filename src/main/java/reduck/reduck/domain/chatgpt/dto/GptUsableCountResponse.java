package reduck.reduck.domain.chatgpt.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GptUsableCountResponse {
    private Long remainUsageCount;
}
