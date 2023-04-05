package reduck.reduck.domain.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.exception.CommonException;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.global.security.JwtProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileImgRepository userProfileImgRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final SignInResponseDtoMapper signInResponseDtoMapper;

    private final String PATH = "C:\\reduckStorage";

    @Transactional
    public void signUp(SignUpDto signUpDto, MultipartFile multipartFile) {
        try {
            User user = userMapper.from(signUpDto);
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            User userEntity = userRepository.save(user);
            saveProfileImage(userEntity, multipartFile);

        } catch (Exception e) {
            UserException userException = new UserException(UserErrorCode.DUPLICATE_USER_ID);
            log.error("회원가입 실패. user id 중복.", userException);
            throw userException;
        }
    }

    @Transactional
    public UserProfileImg saveProfileImage(User user, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        long size = multipartFile.getSize();

        Path imagePath = Paths.get(PATH, storageFileName);
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
            log.error("이미지 저장 실패", e);
            throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public SignInResponseDto signIn(SignInDto dto){

        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_EXIST));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId(), user.getRoles());
        jwtService.saveRefreshToken(refreshToken, user);
        String accessToken = jwtProvider.createToken(user.getUserId(), user.getRoles());
        return signInResponseDtoMapper.of(user, accessToken, refreshToken);

    }

    @Transactional
    public User getUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
    }


}
