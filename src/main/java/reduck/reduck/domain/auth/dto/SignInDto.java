package reduck.reduck.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignInDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String password;

}

