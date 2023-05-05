package reduck.reduck.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationToken {
    public static String getUserId() {
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return userId;
    }
}
