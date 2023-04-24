package reduck.reduck.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDto {
    @NotBlank
    private String userId;

    @NotBlank
    private String name;
    @NotBlank
    private String email;

    private String company;
    private String companyEmail;

    private String school;
    private String schoolEmail;

    @NotBlank
    private String developAnnual;
}