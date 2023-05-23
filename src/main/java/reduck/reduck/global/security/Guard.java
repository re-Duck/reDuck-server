package reduck.reduck.global.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;

import java.util.Optional;

@Component
public class Guard {
    @Autowired
    private UserRepository repo;

    public boolean checkUserId(Authentication authentication, String id) {
        String name = authentication.getName();
        System.out.println(name+" at "+id);
        Optional<User> byUserId = repo.findByUserId(name);
        User result = byUserId.get();
        return result != null && result.getUserId() == id;
    }
}