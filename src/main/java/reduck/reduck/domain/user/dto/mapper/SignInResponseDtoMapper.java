package reduck.reduck.domain.user.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.entity.User;
@Component
@RequiredArgsConstructor
public class SignInResponseDtoMapper {
    public SignInResponseDto of(Object obj, String accessToken, String refreshToken) {
        if (obj instanceof User) {
            User user = (User) obj;
            SignInResponseDto signInResponseDto = SignInResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
            return signInResponseDto;

        }
        return null;
    }
}
