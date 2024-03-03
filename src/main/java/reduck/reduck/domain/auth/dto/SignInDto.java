package reduck.reduck.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class SignInDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String password;

}

