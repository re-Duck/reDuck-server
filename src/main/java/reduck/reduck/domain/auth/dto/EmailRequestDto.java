package reduck.reduck.domain.auth.dto;

import lombok.Getter;

import jakarta.validation.constraints.Email;

@Getter
public class EmailRequestDto {
    @Email
    private String email;
}
