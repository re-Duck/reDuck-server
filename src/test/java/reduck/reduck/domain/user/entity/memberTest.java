package reduck.reduck.domain.user.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reduck.reduck.domain.user.repository.MemberRepository;

@SpringBootTest
class memberTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    void createMemberTest() {
        Member member = new Member();
        member.setMemberId("test1");
        member.setPassword("1234");
        memberRepository.save(member);

        Member member1 = memberRepository.findById(6L).orElseThrow(RuntimeException::new);
        System.out.println("member1 = " + member1);
        member1.setMemberId("test2");
        memberRepository.save(member1);

        Member member2 = memberRepository.findById(6L).orElseThrow(RuntimeException::new);
        System.out.println("member2.getMemberId() = " + member2.getMemberId());
        System.out.println("member2.getUpdatedAt = " + member2.getUpdatedAt());
    }

}