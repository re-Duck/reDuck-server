package reduck.reduck.domain.user.dto;


import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class SignUpDto {
    private String userId;
    private String password;
    private String name;
    private String email;
    private String profileImg;
    private String company;
    private String school;
    private String developAnnual;



}
