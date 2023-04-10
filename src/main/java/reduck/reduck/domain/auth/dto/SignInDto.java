package reduck.reduck.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDto {

    private String userId;
    private String password;

}

