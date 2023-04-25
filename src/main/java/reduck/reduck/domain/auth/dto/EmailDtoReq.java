package reduck.reduck.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class EmailDtoReq {
    @Email
    private String email;

}
