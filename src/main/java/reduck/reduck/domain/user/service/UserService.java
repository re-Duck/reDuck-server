package reduck.reduck.domain.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.Authority;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.entity.UserProfileImg;
import reduck.reduck.domain.user.entity.mapper.UserMapper;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.exception.CommonException;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.UserException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final String PATH = "C:\\reduckStorage";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpDto signUpDto, MultipartFile multipartFile) {
        try {
            UserProfileImg userProfileImg = saveProfileImage(multipartFile);
            encodePasswordOf(signUpDto);
            User user = UserMapper.from(signUpDto);
            user.setProfileImg(userProfileImg);
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            user.setProfileImg(userProfileImg);
            userRepository.save(user);

        } catch (Exception e) {
            System.out.println("e.getCause() = " + e.getCause());
            UserException userException = new UserException(UserErrorCode.DUPLICATE_USER_ID);
            log.error("회원가입 실패. user id 중복.", userException);
            System.out.println("e.getMessage() = " + e.getMessage());
            throw userException;
        }
    }

    private void encodePasswordOf(SignUpDto signUpDto) {
        String password = signUpDto.getPassword();
        String encode = passwordEncoder.encode(password);
        signUpDto.setPassword(encode);

    }

    @Transactional
    public UserProfileImg saveProfileImage(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        long size = multipartFile.getSize();

        Path imagePath = Paths.get(PATH, storageFileName);
        try {
            UserProfileImg userProfileImg = UserProfileImg.builder()
                    .storageFileName(storageFileName)
                    .uploadeFiledName(originalFilename)
                    .path(String.valueOf(imagePath))
                    .extension(extension)
                    .size(size)
                    .build();
            System.out.println("userProfileImg = " + userProfileImg);
            Files.write(imagePath, multipartFile.getBytes());
            return userProfileImg;

        } catch (Exception e) {
            log.error("이미지 저장 실패", e);
            throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
    }


}
