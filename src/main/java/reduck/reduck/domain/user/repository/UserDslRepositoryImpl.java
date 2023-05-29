package reduck.reduck.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.user.entity.QUser;
import reduck.reduck.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

import static reduck.reduck.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserDslRepositoryImpl implements UserDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public User findByUserId(String userId) {
        User user = jpaQueryFactory.selectFrom(QUser.user)
                .where(QUser.user.userId.eq(userId))
                .fetchOne();
        return user;
    }
}
