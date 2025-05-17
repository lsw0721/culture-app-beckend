package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChangePasswordRequestDTO {
    @NotBlank
    private String username;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String newPassword;
}
