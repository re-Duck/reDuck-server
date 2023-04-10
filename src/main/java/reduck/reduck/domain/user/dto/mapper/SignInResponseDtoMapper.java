package reduck.reduck.domain.user.dto.mapper;

import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.entity.User;

public class SignInResponseDtoMapper {
    public static SignInResponseDto of(User obj, String accessToken, String refreshToken) {
        SignInResponseDto signInResponseDto = SignInResponseDto.builder()
                .userId(obj.getUserId())
                .name(obj.getName())
                .email(obj.getEmail())
                .roles(obj.getRoles())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return signInResponseDto;
    }
}
