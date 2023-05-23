package reduck.reduck.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthenticateResponseDto {
   private String emailAuthToken;
}
