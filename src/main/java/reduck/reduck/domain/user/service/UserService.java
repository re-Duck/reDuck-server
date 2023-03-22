package reduck.reduck.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signUp(SignUpDto signUpDto) {
        userRepository.save(User.builder()
                .userId(signUpDto.getUserId())
                .password((signUpDto.getPassword()))
                .build());
        userRepository.findById("test1");
    }
}
