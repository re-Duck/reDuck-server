package reduck.reduck.domain.jwt.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.jwt.dto.AccessTokenDto;
import reduck.reduck.domain.jwt.entity.RefreshToken;
import reduck.reduck.domain.jwt.repository.JwtRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

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
    public AccessTokenDto refreshAccessToken(HttpServletRequest request, String userId) throws Exception {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        try {
            return AccessTokenDto.builder()
                    .accessToken(jwtProvider.refreshAccessToken(request, user))
                    .build();
        } catch (NoSuchElementException e) {
            throw new AuthenticationException("일치하는 토큰이 없음.") {
            };
        }
    }
    @Transactional
    public RefreshToken getRefreshToken(Long userPKId) throws Exception {
        System.out.println("Utils.getRefreshToken");
        List<RefreshToken> allByUserPKId = jwtRepository.findAllByUser_Id(userPKId);
        return allByUserPKId.get(allByUserPKId.size()-1);
    }
}
