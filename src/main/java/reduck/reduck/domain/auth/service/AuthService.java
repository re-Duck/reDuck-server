package reduck.reduck.domain.auth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.auth.dto.AccessTokenDto;
import reduck.reduck.domain.auth.entity.RefreshToken;
import reduck.reduck.domain.auth.repository.AuthRepository;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.dto.mapper.SignInResponseDtoMapper;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.domain.user.service.UserService;
import reduck.reduck.global.exception.errorcode.JwtErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.JwtException;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.global.security.JwtProvider;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public SignInResponseDto signIn(SignInDto dto){

        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_EXIST));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId(), user.getRoles());
        saveRefreshToken(refreshToken, user);
        String accessToken = jwtProvider.createToken(user.getUserId(), user.getRoles());
        return SignInResponseDtoMapper.of(user, accessToken, refreshToken);

    }



    @Transactional
    public void saveRefreshToken(String token, User user) {
        RefreshToken refreshToken = RefreshToken.builder().refreshToken(token).user(user).build();
        authRepository.save(refreshToken);

    }

    @Transactional
    public AccessTokenDto reissuanceAccessToken(HttpServletRequest request, String userId) throws Exception {

//        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        User user = userService.findByUserId(userId);
        String findRefreshToken = findAllByUserPk(user.getId());
        String requestRefreshToken = jwtProvider.resolveToken(request);
        jwtProvider.validateToken(requestRefreshToken);

        if (isSameRefreshToken(findRefreshToken, requestRefreshToken)) {
            String newAccessToken = jwtProvider.createToken(user.getUserId(), user.getRoles());
            return AccessTokenDto.builder().accessToken(newAccessToken).build();
        }
        throw new JwtException(JwtErrorCode.TOKEN_NOT_EXIST);

    }

    @Transactional
    public String findAllByUserPk(Long userPKId) throws Exception {
        List<RefreshToken> allByUserPKId = authRepository.findAllByUser_Id(userPKId);
        return allByUserPKId.get(allByUserPKId.size() - 1).getRefreshToken();
    }

    private boolean isSameRefreshToken(String findRefreshToken, String requestRefreshToken) {
        String refreshToken = requestRefreshToken.split(" ")[1].trim();
        return findRefreshToken.equals(refreshToken);
    }
}