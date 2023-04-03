package reduck.reduck.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.jwt.service.JwtService;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.dto.mapper.SignInResponseDtoMapper;
import reduck.reduck.domain.user.entity.Authority;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.entity.UserProfileImg;
import reduck.reduck.domain.user.entity.mapper.UserMapper;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.security.JwtProvider;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final SignInResponseDtoMapper signInResponseDtoMapper;

    @Transactional
    public void signUp(SignUpDto signUpDto) throws Exception {
        try {
            User user = userMapper.from(signUpDto);
//            User user = User.builder()
//                    .userId(signUpDto.getUserId())
//                    .password(passwordEncoder.encode(signUpDto.getPassword()))
//                    .name(signUpDto.getName())
//                    .email(signUpDto.getEmail())
//                    .profileImg(signUpDto.getProfileImg())
//                    .company(signUpDto.getCompany())
//                    .school(signUpDto.getSchool())
//                    .developAnnual(DevelopAnnual.getAnnual(signUpDto.getDevelopAnnual()))
//                    .build();
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
    }
    @Transactional
    public UserProfileImg saveImage(MultipartFile multipartFile) throws ServletException, IOException {
        System.out.println("multipartFile = " + multipartFile);
        String originalFilename = multipartFile.getOriginalFilename();
        String storageName = UUID.randomUUID() + ".jpeg";

        String path = "C:\\reduckStorage";
        Path imagePath = Paths.get(path, storageName);
        UserProfileImg userProfileImg = UserProfileImg.builder()
                .storageName(storageName)
                .uploadedName(originalFilename)
                .path(String.valueOf(imagePath))
                .build();
        try {
            Files.write(imagePath, multipartFile.getBytes());
            // user profile img repository save.
            //return : Long id
            return userProfileImg;

        } catch (Exception e) {
            System.out.println("e.getCause() = " + e.getCause());
            System.out.println("e.getMessage() = " + e.getMessage());
            return null;
        }
    }
    @Transactional
    public SignInResponseDto signIn(SignInDto dto) throws IOException {

        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId(), user.getRoles());
        jwtService.saveRefreshToken(refreshToken, user);
        String accessToken = jwtProvider.createToken(user.getUserId(), user.getRoles());
        SignInResponseDto of = signInResponseDtoMapper.of(user, accessToken, refreshToken);
        System.out.println("of = " + of);
        return of;
//        return SignInResponseDto.builder()
//                .userId(user.getUserId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .roles(user.getRoles())
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
    }

    @Transactional
    public User getUser(String userId) throws Exception {
        System.out.println("UserService.getUser");
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        System.out.println("user = " + user);
        return user;
    }


}