package reduck.reduck.domain.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.user.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
