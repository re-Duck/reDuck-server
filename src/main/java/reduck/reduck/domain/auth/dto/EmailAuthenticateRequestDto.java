package reduck.reduck.domain.auth.dto;

import lombok.Getter;
import reduck.reduck.domain.auth.entity.EmailType;

import jakarta.validation.constraints.*;

@Getter
public class EmailAuthenticateRequestDto {
    @Email
    private String email;

    @NotNull
    private EmailType type;

    @NotNull @Max(999999) @Min(100000)
    int number;
}
