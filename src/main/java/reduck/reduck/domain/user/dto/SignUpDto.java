package reduck.reduck.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
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
