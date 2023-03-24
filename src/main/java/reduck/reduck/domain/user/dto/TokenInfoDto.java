package reduck.reduck.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfoDto {
    private String grantType; //jwt 에 대한 인증타입, HTTP헤더에 붙여주는 prefix
    private String accessToken;
    private String refreshToken;
}
