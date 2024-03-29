package reduck.reduck.domain.user.service;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.auth.entity.EmailType;
import reduck.reduck.domain.chatgpt.entity.ChatGpt;
import reduck.reduck.domain.chatgpt.repository.ChatGptRepository;
import reduck.reduck.domain.user.dto.ModifyUserDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.dto.UserInfoDtoRes;
import reduck.reduck.domain.user.dto.mapper.UserInfoDtoResMapper;
import reduck.reduck.domain.user.entity.Authority;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.entity.UserProfileImg;
import reduck.reduck.domain.user.entity.mapper.UserMapper;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.AuthErrorCode;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.exception.*;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.IllegalStateException;
import reduck.reduck.global.security.JwtProvider;
import reduck.reduck.util.AuthenticationToken;
import reduck.reduck.util.Encoder;

import java.io.File;
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
    private static final String PATH = "C:\\reduckStorage\\profile";
    private static final String DEV_PATH = "/home/ubuntu/reduck/storage/profile";
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ChatGptRepository chatGptRepository;

    @Transactional
    public User signUp(SignUpDto signUpDto, MultipartFile multipartFile) {
        try {
            Encoder.encodePasswordOf(signUpDto);
            validateSignUpDto(signUpDto);
            User user = UserMapper.from(signUpDto);
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            if (!multipartFile.isEmpty()) {
                UserProfileImg userProfileImg = saveProfileImage(multipartFile, signUpDto.getUserId());
                user.updateProfileImg(userProfileImg);
            }
            User userEntity = userRepository.save(user);
            //gpt 멤버십 등록
            ChatGpt defaultChatGptMembership = ChatGpt.builder().user(user).build();
            chatGptRepository.save(defaultChatGptMembership);

            return userEntity;
        } catch (CommonException | AuthException | DataIntegrityViolationException e) {
            log.error("회원가입 에러", e);
            if (e instanceof DataIntegrityViolationException) {
                throw new IllegalStateException("중복된 사용자 id 입니다.");
            }
            throw e;
        }
    }

    @Transactional
    public boolean isDuplicatedUserId(String userId) {
        Optional<User> byUserId = userRepository.findByUserId(userId);
        return byUserId.isPresent();
    }

    @Transactional
    public UserInfoDtoRes modifyUserInfo(ModifyUserDto modifyUserDto, MultipartFile multipartFile) {
        String userId = AuthenticationToken.getUserId();
        User user = findByUserId(userId);
        if (Encoder.validate(modifyUserDto.getPassword(), user.getPassword())) {
            modifyUserDto.setPassword(
                    modifyUserDto.getNewPassword().equals("")
                            ? modifyUserDto.getPassword()
                            : modifyUserDto.getNewPassword());
            Encoder.encodePasswordOf(modifyUserDto);
        }
        try {
            if (!multipartFile.isEmpty()) {
                UserProfileImg userProfileImg = saveProfileImage(multipartFile, userId);
                user.updateProfileImg(userProfileImg);
            }
            validateUserEmail(modifyUserDto, user);
            if (validateCompanyEmail(modifyUserDto, user)) user.authenticateCompanyEmail();
            if (validateSchoolEmail(modifyUserDto, user)) user.authenticateSchoolEmail();
            user.updateFrom(modifyUserDto);
            User save = userRepository.save(user);
            UserInfoDtoRes userInfo = UserInfoDtoResMapper.from(save);
            return userInfo;
        } catch (CommonException | AuthException | DataIntegrityViolationException e) {
            throw e;
        }

    }

    @Transactional
    public void withdraw() {
        User user = findByUserId(AuthenticationToken.getUserId());
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            log.error("회원탈퇴 에러 : ", e);
            throw e;
        }
    }

    @Transactional
    public UserInfoDtoRes getUser(String userId) {
        return getUserInfo(userId);
    }

    @Transactional
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));
    }

    private void validateSignUpDto(SignUpDto signUpDto) {
        String email = signUpDto.getEmail();

        String emailAuthToken = signUpDto.getEmailAuthToken();
        Claims claims = jwtProvider.getClaims(emailAuthToken);
        String userEmail = String.valueOf(claims.get(EmailType.USER.name()));
        System.out.println(userEmail);
        System.out.println(email);
        if (!email.equals(userEmail)) throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
    }

    private boolean validateSchoolEmail(ModifyUserDto modifyUserDto, User user) {
        String schoolEmail = modifyUserDto.getSchoolEmail();
        if (!isModifyAble(schoolEmail, user.getSchoolEmail())) {
            return false;
        }
        String emailAuthToken = modifyUserDto.getSchoolEmailAuthToken();
        validateModifyUserDtoEmailAuthToken(emailAuthToken);
        String authEmail = String.valueOf(jwtProvider.getClaims(emailAuthToken).get(EmailType.SCHOOL.name()));
        validateModifyUserDtoEmailAuthToken(authEmail, schoolEmail);
        return true;
    }

    private boolean validateCompanyEmail(ModifyUserDto modifyUserDto, User user) {
        String companyEmail = modifyUserDto.getCompanyEmail();
        if (!isModifyAble(companyEmail, user.getCompanyEmail())) {
            return false;
        }
        String emailAuthToken = modifyUserDto.getCompanyEmailAuthToken();
        validateModifyUserDtoEmailAuthToken(emailAuthToken);
        String authEmail = String.valueOf(jwtProvider.getClaims(emailAuthToken).get(EmailType.COMPANY.name()));
        validateModifyUserDtoEmailAuthToken(authEmail, companyEmail);
        return true;
    }

    private boolean validateUserEmail(ModifyUserDto modifyUserDto, User user) {
        String userEmail = modifyUserDto.getEmail();
        if (!isModifyAble(userEmail, user.getEmail())) {
            return false;
        }
        String emailAuthToken = modifyUserDto.getEmailAuthToken();
        validateModifyUserDtoEmailAuthToken(emailAuthToken);
        String authEmail = String.valueOf(jwtProvider.getClaims(emailAuthToken).get(EmailType.USER.name()));
        validateModifyUserDtoEmailAuthToken(authEmail, userEmail);
        return true;
    }

    private UserProfileImg saveProfileImage(MultipartFile multipartFile, String userId) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        long size = multipartFile.getSize();
        String path = DEV_PATH + "/" + userId; //폴더 경로
        File Folder = new File(path);
        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try {
                Folder.mkdir(); //폴더 생성합니다.
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        Path imagePath = Paths.get(path, storageFileName);
        try {
            UserProfileImg userProfileImg = UserProfileImg.builder()
                    .storagedFileName(storageFileName)
                    .uploadedFileName(originalFilename)
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

    private void validateModifyUserDtoEmailAuthToken(String emailAuthToken) {
        // 새롭게 추가될 이메일 || 존재하지만 수정될 이메일. ==> 수정 대상 이메일
        if (emailAuthToken.isEmpty()) throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
    }

    private void validateModifyUserDtoEmailAuthToken(String authEmail, String email) {
        // (추가||수정 될 이메일) && 인증완료.
        if (!authEmail.equals(email)) throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
    }

    private boolean isModifyAble(String email, String originEmail) {
        if (email.isEmpty()) return false; // 입력 된적x, 수정하지도 않겠다.
        if (originEmail != null && email.equals(originEmail)) return false; //이메일이 같음 -> 수정하지 않겠음.
        return true;
    }

    private UserInfoDtoRes getUserInfo(String userId) {
        User user = findByUserId(userId);
        UserInfoDtoRes userInfoDtoRes = UserInfoDtoResMapper.from(user);
        return userInfoDtoRes;
    }

}