package reduck.reduck.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class SchoolEmailRequestDto {
    @Email
    private String schoolEmail;
    @NotBlank
    private String userId;
    @NotNull @Max(999999) @Min(100000)
    int number;
}
