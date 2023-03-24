package reduck.reduck.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signUp(SignUpDto signUpDto) {
        System.out.println("signUpDto.getDevelopAnnual() = " + signUpDto.getDevelopAnnual());
        userRepository.save(User.builder()
                .userId(signUpDto.getUserId())
                .password((signUpDto.getPassword()))
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .profileImg(signUpDto.getProfileImg())
                .company(signUpDto.getCompany())
                .school(signUpDto.getSchool())
                .developAnnual(DevelopAnnual.getAnnual(signUpDto.getDevelopAnnual()))
                .build());
//        userRepository.findById("test1");
    }
}
