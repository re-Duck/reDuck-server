package reduck.reduck.domain.user.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.jwt.entity.RefreshToken;
import reduck.reduck.domain.jwt.repository.JwtRepository;
import reduck.reduck.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Utils {
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;

    public RefreshToken getRefreshToken(Long userPKId) throws Exception {
        System.out.println("Utils.getRefreshToken");
        List<RefreshToken> allByUserPKId = jwtRepository.findAllByUser_Id(userPKId);
        return allByUserPKId.get(allByUserPKId.size()-1);
    }

}
