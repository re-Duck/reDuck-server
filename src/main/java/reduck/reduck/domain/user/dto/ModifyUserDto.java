package reduck.reduck.domain.user.dto;


import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDto {

    @NotBlank
    private String password;
    private String newPassword;

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
