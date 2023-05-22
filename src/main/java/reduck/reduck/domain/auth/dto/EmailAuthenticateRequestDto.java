package reduck.reduck.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class EmailAuthenticateRequestDto {
    @Email
    private String email;

    @NotBlank
    private String type;

    @NotNull @Max(999999) @Min(100000)
    int number;
}
