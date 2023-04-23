package reduck.reduck.domain.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.auth.entity.EmailAuthentication;
import reduck.reduck.domain.auth.repository.EmailAuthenticationRepository;
import reduck.reduck.domain.user.dto.ModifyUserDto;
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
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final String PATH = "C:\\reduckStorage";
    private static final String DEV_PATH = "/home/nuhgnod/develup/storage";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthenticationRepository emailAuthenticationRepository;

    @Transactional
    public User signUp(SignUpDto signUpDto, MultipartFile multipartFile) {
        try {
            encodePasswordOf(signUpDto);
            User user = UserMapper.from(signUpDto);
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            if (!multipartFile.isEmpty()) {
                UserProfileImg userProfileImg = saveProfileImage(multipartFile);
                user.setProfileImg(userProfileImg);
            }
            User userEntity = userRepository.save(user);
            return userEntity;
        } catch (CommonException | DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Transactional
    public User modifyUserInfo(ModifyUserDto modifyUserDto, MultipartFile multipartFile) {
        String userId = modifyUserDto.getUserId();
        User userByUserId = findByUserId(userId);
        try {
            if (!multipartFile.isEmpty()) {
                UserProfileImg userProfileImg = saveProfileImage(multipartFile);
                userByUserId.updateProfileImg(userProfileImg);

            }
            userByUserId.updateFrom(modifyUserDto);
            User save = userRepository.save(userByUserId);
            return save;
        } catch (CommonException | DataIntegrityViolationException e) {
            throw e;
        }

    }
    @Transactional
    public void authenticateCompanyEmail(String userId, String companyEmail, int number) {
        User user = findByUserId(userId);
        Optional<EmailAuthentication> emailAuthentication = emailAuthenticationRepository.findTopByEmailOrderByIdDesc(companyEmail);
        if (emailAuthentication.get().getAuthenticationNumber() == number) {
            user.authenticatedCompanyEmail();
            User save = userRepository.save(user);

            return;
        }
        throw new CommonException(CommonErrorCode.IS_NOT_MATCH);

    }
    @Transactional
    public void authenticateSchoolEmail(String userId, String schoolEmail, int number) {
        User user = findByUserId(userId);
        Optional<EmailAuthentication> emailAuthentication = emailAuthenticationRepository.findTopByEmailOrderByIdDesc(schoolEmail);
        if (emailAuthentication.get().getAuthenticationNumber() == number) {
            user.authenticatedSchoolEmail();
            User save = userRepository.save(user);

            return;
        }
        throw new CommonException(CommonErrorCode.IS_NOT_MATCH);

    }
    @Transactional
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
    }


    private UserProfileImg saveProfileImage(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        long size = multipartFile.getSize();

        Path imagePath = Paths.get(PATH, storageFileName); //local용
//        Path imagePath = Paths.get(DEV_PATH, storageFileName); //dev용
        try {
            UserProfileImg userProfileImg = UserProfileImg.builder()
                    .storageFileName(storageFileName)
                    .uploadeFiledName(originalFilename)
                    .path(String.valueOf(imagePath))
                    .extension(extension)
                    .size(size)
                    .build();
            Files.write(imagePath, multipartFile.getBytes());
            return userProfileImg;

        } catch (Exception e) {
            log.error("이미지 저장 실패", e);
            throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void encodePasswordOf(SignUpDto signUpDto) {
        String password = signUpDto.getPassword();
        String encode = passwordEncoder.encode(password);
        signUpDto.setPassword(encode);

    }

}