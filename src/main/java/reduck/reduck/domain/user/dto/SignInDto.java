package reduck.reduck.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInDto {

    private String userId;
    private String password;

}

