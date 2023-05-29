package reduck.reduck.domain.user.repository;

import reduck.reduck.domain.user.entity.User;

import java.util.Optional;

public interface UserDslRepository {
    User findByUserId(String userId);

}
