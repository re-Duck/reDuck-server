package reduck.reduck.domain.jwt.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.jwt.dto.AccessTokenDto;
import reduck.reduck.domain.jwt.entity.RefreshToken;
import reduck.reduck.domain.jwt.repository.JwtRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.JwtErrorCode;
import reduck.reduck.global.exception.exception.JwtException;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.global.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void saveRefreshToken(String token, User user) {
        RefreshToken refreshToken = RefreshToken.builder().refreshToken(token).user(user).build();
        jwtRepository.save(refreshToken);

    }

    @Transactional
    public AccessTokenDto reissuanceAccessToken(HttpServletRequest request, String userId) throws Exception {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        String findRefreshToken = getRefreshToken(user.getId());
        String requestRefreshToken = jwtProvider.resolveToken(request);
        jwtProvider.validateToken(requestRefreshToken);

        if (isSameRefreshToken(findRefreshToken, requestRefreshToken)) {
            String newAccessToken = jwtProvider.createToken(user.getUserId(), user.getRoles());
            return AccessTokenDto.builder().accessToken(newAccessToken).build();
        }
        throw new JwtException(JwtErrorCode.TOKEN_NOT_EXIST);

    }

    @Transactional
    public String getRefreshToken(Long userPKId) throws Exception {
        List<RefreshToken> allByUserPKId = jwtRepository.findAllByUser_Id(userPKId);
        return allByUserPKId.get(allByUserPKId.size() - 1).getRefreshToken();
    }

    private boolean isSameRefreshToken(String findRefreshToken, String requestRefreshToken) {
        String refreshToken = requestRefreshToken.split(" ")[1].trim();
        return findRefreshToken.equals(refreshToken);
    }
}
