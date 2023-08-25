package reduck.reduck.domain.chat.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@NoArgsConstructor
public class RecommendUserDto {
    private String userId;
    private String name;
    private String userProfileImgPath;
    private String company;
    private boolean companyEmailAuthentication;
    private String developAnnual;

    @Builder
    public RecommendUserDto(String userId, String name,String userProfileImgPath, String company, boolean companyEmailAuthentication, String developAnnual) {
        this.userId = userId;
        this.name = name;
        this.userProfileImgPath = userProfileImgPath;
        this.company = company;
        this.companyEmailAuthentication = companyEmailAuthentication;
        this.developAnnual = developAnnual;
    }
}
