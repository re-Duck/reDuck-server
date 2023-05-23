package reduck.reduck.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDto {


    @NotBlank
    private String name;
    @NotBlank
    private String email;

    private String emailAuthToken;

    private String company;
    @Email
    private String companyEmail;
    private String companyEmailAuthToken;

    private String school;
    @Email
    private String schoolEmail;
    private String schoolEmailAuthToken;

    @NotNull
    private int developYear;
}
