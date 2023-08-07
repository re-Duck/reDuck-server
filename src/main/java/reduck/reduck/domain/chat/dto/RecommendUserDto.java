package reduck.reduck.domain.chat.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@NoArgsConstructor
public class RecommendUserDto {
    private String userId;
    private String profilePath;
    private String company;
    private boolean companyAuthentication;
    private String developYear;

    @Builder
    public RecommendUserDto(String userId, String profilePath, String company, boolean companyAuthentication, String developYear) {
        this.userId = userId;
        this.profilePath = profilePath;
        this.company = company;
        this.companyAuthentication = companyAuthentication;
        this.developYear = developYear;
    }
}
