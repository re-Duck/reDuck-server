package reduck.reduck.domain.auth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.auth.dto.AccessTokenDto;
import reduck.reduck.domain.auth.entity.RefreshToken;
import reduck.reduck.domain.auth.repository.AuthRepository;
import reduck.reduck.domain.auth.dto.SignInDto;
import reduck.reduck.domain.auth.dto.SignInResponseDto;
import reduck.reduck.domain.auth.dto.mapper.SignInResponseDtoMapper;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.domain.user.service.UserService;
import reduck.reduck.global.exception.errorcode.AuthErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.AuthException;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.global.security.JwtProvider;
import reduck.reduck.util.AuthenticationToken;

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

        User user = userService.findByUserId(dto.getUserId());
        validatePassword(dto.getPassword(),user.getPassword());
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
    public AccessTokenDto reissuanceAccessToken(HttpServletRequest request) throws Exception {
        String userId = AuthenticationToken.getUserId();
        User user = userService.findByUserId(userId);
        String findRefreshToken = findAllByUserPk(user.getId());
        String requestRefreshToken = jwtProvider.resolveToken(request);
        jwtProvider.validateToken(requestRefreshToken);

        if (isSameRefreshToken(findRefreshToken, requestRefreshToken)) {
            String newAccessToken = jwtProvider.createToken(user.getUserId(), user.getRoles());
            return AccessTokenDto.builder().accessToken(newAccessToken).build();
        }
        throw new AuthException(AuthErrorCode.TOKEN_NOT_EXIST);

    }

    @Transactional
    public String findAllByUserPk(Long userPKId){
        List<RefreshToken> allByUserPKId = authRepository.findAllByUser_Id(userPKId);
        return allByUserPKId.get(allByUserPKId.size() - 1).getRefreshToken();
    }

    private boolean isSameRefreshToken(String findRefreshToken, String requestRefreshToken) {
        String refreshToken = requestRefreshToken.split(" ")[1].trim();
        return findRefreshToken.equals(refreshToken);
    }

    private boolean validatePassword(String originPassword, String targetPassword) {
        System.out.println("originPassword = " + originPassword);
        System.out.println("targetPassword = " + targetPassword);
        if (!passwordEncoder.matches(originPassword,targetPassword)) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
        return true;
    }
}
