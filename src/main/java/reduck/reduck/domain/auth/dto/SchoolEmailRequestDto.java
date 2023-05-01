package reduck.reduck.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class SchoolEmailRequestDto {
    @Email
    private String schoolEmail;
    @NotBlank
    private String userId;
    @NotNull
    int number;
}
