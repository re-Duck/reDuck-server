package reduck.reduck.domain.user.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reduck.reduck.domain.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
class userTest {
    @Autowired
    UserRepository memberRepository;

    @Test
    void createMemberTest() {

        memberRepository.save(User.builder().userId("test1").password("1234").build());

        User member1 = memberRepository.findById("test1").orElseThrow(RuntimeException::new);
        System.out.println("member1 = " + member1);
//        member1.setUserId("test2");
//        memberRepository.save(member1);
//
//        User member2 = memberRepository.findById("test1").orElseThrow(RuntimeException::new);
//        System.out.println("member2.getMemberId() = " + member2.getUserId());
//        System.out.println("member2.getUpdatedAt = " + member2.getUpdatedAt());
    }

}