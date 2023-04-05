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
import reduck.reduck.domain.user.repository.UserProfileImgRepository;
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
    private final UserProfileImgRepository userProfileImgRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final SignInResponseDtoMapper signInResponseDtoMapper;

    @Transactional
    public void signUp(SignUpDto signUpDto, MultipartFile multipartFile) throws Exception {
        try {
            System.out.println("UserService.signUp");
            User user = userMapper.from(signUpDto);
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            User userEntity = userRepository.save(user);
            saveProfileImage(userEntity,multipartFile);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
    }
    @Transactional
    public UserProfileImg saveProfileImage(User user,MultipartFile multipartFile) throws ServletException, IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        long size = multipartFile.getSize();
        String path = "C:\\reduckStorage";

        Path imagePath = Paths.get(path, storageFileName);
        UserProfileImg userProfileImg = UserProfileImg.builder()
                .storageFileName(storageFileName)
                .uploadeFiledName(originalFilename)
                .path(String.valueOf(imagePath))
                .extension(extension)
                .size(size)
                .user(user)
                .build();
        try {
            Files.write(imagePath, multipartFile.getBytes());
            UserProfileImg profileImg = userProfileImgRepository.save(userProfileImg);
            return profileImg;

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
        return signInResponseDtoMapper.of(user, accessToken, refreshToken);

    }

    @Transactional
    public User getUser(String userId) throws Exception {
        return userRepository.findByUserId(userId).orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
    }


}
