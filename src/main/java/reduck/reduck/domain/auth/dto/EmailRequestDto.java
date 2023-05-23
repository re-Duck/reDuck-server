package reduck.reduck.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class EmailRequestDto {
    @Email
    private String email;
}
