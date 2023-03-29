package reduck.reduck.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.jwt.service.JwtService;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.Authority;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.security.JwtProvider;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    @Transactional
    public boolean signUp(SignUpDto signUpDto) throws Exception {
        try {
            User user = User.builder()
                    .userId(signUpDto.getUserId())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .name(signUpDto.getName())
                    .email(signUpDto.getEmail())
                    .profileImg(signUpDto.getProfileImg())
                    .company(signUpDto.getCompany())
                    .school(signUpDto.getSchool())
                    .developAnnual(DevelopAnnual.getAnnual(signUpDto.getDevelopAnnual()))
                    .build();
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public SignInResponseDto signIn(SignInDto dto) throws IOException {

        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }
        String refreshToken = jwtProvider.createRefreshToken();
        jwtService.saveRefreshToken(refreshToken, user);

        return SignInResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .accessToken(jwtProvider.createToken(user.getUserId(), user.getRoles()))
                .refreshToken(refreshToken)
                .build();
    }

    public User getUser(String userId) throws Exception {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new Exception("계정을 찾을 수 없는니다."));
        return user;
    }

}
