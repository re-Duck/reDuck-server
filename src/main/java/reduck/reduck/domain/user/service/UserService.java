package reduck.reduck.domain.user.service;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
import reduck.reduck.global.exception.exception.AuthException;
import reduck.reduck.global.exception.exception.CommonException;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.global.security.JwtProvider;
import reduck.reduck.util.AuthenticationToken;

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
    private static final String DEV_PATH = "/home/nuhgnod/develup/storage/profile";
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(SignUpDto signUpDto, MultipartFile multipartFile) {
        try {
            encodePasswordOf(signUpDto);
            validateSignUpDto(signUpDto);
            User user = UserMapper.from(signUpDto);
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            if (!multipartFile.isEmpty()) {
                UserProfileImg userProfileImg = saveProfileImage(multipartFile, signUpDto.getUserId());
                user.updateProfileImg(userProfileImg);
            }
            User userEntity = userRepository.save(user);
            return userEntity;
        } catch (CommonException | AuthException | DataIntegrityViolationException e) {
            log.error("회원가입 에러", e);
            throw e;
        }
    }

    @Transactional
    public boolean isDuplicatedUserId(String userId) {
        Optional<User> byUserId = userRepository.findByUserId(userId);
        return byUserId.isPresent();
    }

    @Transactional
    public User modifyUserInfo(ModifyUserDto modifyUserDto, MultipartFile multipartFile) {
        String userId = AuthenticationToken.getUserId();
        User user = findByUserId(userId);

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
            return save;
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
    public UserInfoDtoRes getMyInfo() {
        String userId = AuthenticationToken.getUserId();
        return getUserInfo(userId);

    }

    @Transactional
    public UserInfoDtoRes getUser(String userId) {
        return getUserInfo(userId);
    }

    @Transactional
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
    }

    private void validateSignUpDto(SignUpDto signUpDto) {
        String email = signUpDto.getEmail();
        String emailAuthToken = signUpDto.getEmailAuthToken();
        Claims claims = jwtProvider.getClaims(emailAuthToken);
        String userEmail = String.valueOf(claims.get("user"));
        if (!email.equals(userEmail)) throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
    }

    private void validateModifyUserDto(ModifyUserDto modifyUserDto, User user) {
        validateUserEmail(modifyUserDto, user);
        validateCompanyEmail(modifyUserDto, user);
        validateSchoolEmail(modifyUserDto, user);

    }

    private boolean validateSchoolEmail(ModifyUserDto modifyUserDto, User user) {
        String schoolEmail = modifyUserDto.getSchoolEmail();
        if (schoolEmail.equals("")) {
            // 입력 된적도 없다.
            // 수정하지도 않겠다.
            return false;
        }
        // 입력 대상인가 ? 기존 메일인가(수정)
        String originSchoolEmail = user.getSchoolEmail();
        if (!(originSchoolEmail == null) && schoolEmail.equals(originSchoolEmail)) {
            // 유저의 기존 메일 존재 && (두 메일 정보가 같음 -> 수정하지 않겠다.)
            return false;
        }
        // 새롭게 추가될 이메일 || 존재하지만 수정될 이메일. ==> 수정 대상 이메일
        String emailAuthToken = modifyUserDto.getSchoolEmailAuthToken();
        if (emailAuthToken.isEmpty()) {
            // 인증 식별자가 존재하지 않음.
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
        }
        Claims claims = jwtProvider.getClaims(emailAuthToken);
        String authEmail = String.valueOf(claims.get("school"));

        // (추가||수정 될 이메일) && 인증완료.
        if (!authEmail.equals(schoolEmail)) {
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
        }
        return true;

    }

    private boolean validateCompanyEmail(ModifyUserDto modifyUserDto, User user) {
        String companyEmail = modifyUserDto.getCompanyEmail();
        if (companyEmail.equals("")) {
            // 입력 된적도 없다.
            // 수정하지도 않겠다.
            return false;
        }
        // 입력 대상인가 ? 기존 메일인가(수정)
        String originCompanyEmail = user.getCompanyEmail();


        if (!(originCompanyEmail == null) && companyEmail.equals(originCompanyEmail)) {
            // 유저의 기존 메일 존재 && (두 메일 정보가 같음 -> 수정하지 않겠다.)
            return false;
        }
        // 새롭게 추가될 이메일 || 존재하지만 수정될 이메일. ==> 수정 대상 이메일
        String emailAuthToken = modifyUserDto.getCompanyEmailAuthToken();
        if (emailAuthToken.isEmpty()) {
            // 인증 식별자가 존재하지 않음.
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
        }
        Claims claims = jwtProvider.getClaims(emailAuthToken);
        String authEmail = String.valueOf(claims.get("school"));

        // (추가||수정 될 이메일) && 인증완료.
        if (!authEmail.equals(companyEmail)) {
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
        }
        return true;
    }

    private boolean validateUserEmail(ModifyUserDto modifyUserDto, User user) {
        // 기본적으로 인증완료된 이메일를 가지고 있음.

        // modifyUserDto의 email정보와 user의 Email의 일치여부만 검증하며됨.
        String targetEmail = modifyUserDto.getEmail();
        String originEmail = user.getEmail();
        if (originEmail.equals(targetEmail)) {
            // 수정하지 않겠음.
            return false;
        }
        // 정보가 다름 -> 수정대상 이메일
        String emailAuthToken = modifyUserDto.getEmailAuthToken();
        if (emailAuthToken.isEmpty()) {
            // 인증 식별자가 존재하지 않음.
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
        }
        Claims claims = jwtProvider.getClaims(emailAuthToken);
        String authEmail = String.valueOf(claims.get("user"));
        if (!authEmail.equals(targetEmail)) {
            throw new AuthException(AuthErrorCode.UNAUTHENTICATED_EMAIL);
        }
        return true;
    }

    private UserProfileImg saveProfileImage(MultipartFile multipartFile, String userId) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        long size = multipartFile.getSize();
        String path = PATH + "/" + userId; //폴더 경로
        File Folder = new File(path);
        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try {
                Folder.mkdir(); //폴더 생성합니다.
                System.out.println("폴더가 생성되었습니다.");
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            System.out.println("이미 폴더가 생성되어 있습니다.");
        }
        Path imagePath = Paths.get(path, storageFileName); //local용
//        Path imagePath = Paths.get(DEV_PATH, storageFileName); //dev용
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

    private void encodePasswordOf(SignUpDto signUpDto) {
        String password = signUpDto.getPassword();
        String encode = passwordEncoder.encode(password);
        signUpDto.setPassword(encode);
    }

    private UserInfoDtoRes getUserInfo(String userId) {
        User user = findByUserId(userId);
        UserInfoDtoRes userInfoDtoRes = UserInfoDtoResMapper.from(user);
        return userInfoDtoRes;
    }
}