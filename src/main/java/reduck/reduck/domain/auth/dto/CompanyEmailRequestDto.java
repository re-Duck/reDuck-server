package reduck.reduck.domain.auth.dto;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class CompanyEmailRequestDto {
    @Email
    private String companyEmail;
    @NotBlank
    private String userId;
    @NotNull @Max(999999) @Min(100000)
    int number;
}
