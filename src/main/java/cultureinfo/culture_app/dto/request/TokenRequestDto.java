package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRequestDto {
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String accessToken;

}
