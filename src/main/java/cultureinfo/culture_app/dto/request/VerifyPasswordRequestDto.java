package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyPasswordRequestDto {
    @NotBlank
    private String password;
}
