package reduck.reduck.domain.chat.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@NoArgsConstructor
public class RecommendUserDto {
    private String userId;
    private String name;
    private String userProfileImagePath;
    private String company;
    private boolean companyEmailAuthentication;
    private String developAnnual;

    @Builder
    public RecommendUserDto(String userId, String name,String userProfileImagePath, String company, boolean companyEmailAuthentication, String developAnnual) {
        this.userId = userId;
        this.name = name;
        this.userProfileImagePath = userProfileImagePath;
        this.company = company;
        this.companyEmailAuthentication = companyEmailAuthentication;
        this.developAnnual = developAnnual;
    }
}
