package reduck.reduck.domain.auth.dto.mapper;

import reduck.reduck.domain.auth.dto.SignInResponseDto;
import reduck.reduck.domain.user.entity.User;

public class SignInResponseDtoMapper {
    public static SignInResponseDto of(User obj, String accessToken, String refreshToken) {
        SignInResponseDto signInResponseDto = SignInResponseDto.builder()
                .userId(obj.getUserId())
                .name(obj.getName())
                .email(obj.getEmail())
                .roles(obj.getRoles())
                .userProfileImg(obj.getProfileImg())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return signInResponseDto;
    }
}
