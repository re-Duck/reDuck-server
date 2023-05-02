package reduck.reduck.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class UserEmailRequestDto {
    @Email
    private String email;
    @NotNull @Max(999999) @Min(100000)
    int number;
}
