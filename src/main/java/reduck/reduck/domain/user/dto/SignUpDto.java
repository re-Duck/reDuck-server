package reduck.reduck.domain.user.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String userId;
    private String password;
    private String name;
    private String email;
//    private String profileImg;
    private String company;
    private String school;
    private String developAnnual;



}
