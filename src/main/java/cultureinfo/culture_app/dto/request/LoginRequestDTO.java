package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}