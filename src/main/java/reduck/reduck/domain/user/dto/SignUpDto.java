package reduck.reduck.domain.user.dto;


import lombok.*;

import jakarta.validation.constraints.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {


    @NotBlank
    private String userId;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$", message = "비밀번호 사이의 숫자,영문자,특수문자의 조합인 8~15의 글자로 이루어져야합니다")
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

    private String company;

    private String school;

    @NotNull
    private int developYear;

    @NotBlank
    private String emailAuthToken;

}
