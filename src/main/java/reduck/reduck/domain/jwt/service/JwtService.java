package reduck.reduck.domain.jwt.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.jwt.entity.RefreshToken;
import reduck.reduck.domain.jwt.repository.JwtRepository;
import reduck.reduck.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtRepository jwtRepository;

    public void saveRefreshToken(String token, User user) {
        RefreshToken refreshToken = RefreshToken.builder().refreshToken(token).user(user).build();
        jwtRepository.save(refreshToken);

    }
}
