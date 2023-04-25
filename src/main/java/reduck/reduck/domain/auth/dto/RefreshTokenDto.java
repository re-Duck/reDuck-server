package reduck.reduck.domain.auth.dto;


import lombok.Builder;


@Builder
public class RefreshTokenDto {

    private Long userPKId;

    private String token;

    protected RefreshTokenDto() {
    }

    public RefreshTokenDto(Long userPKId, String token) {
        this.userPKId = userPKId;
        this.token = token;
    }
}
