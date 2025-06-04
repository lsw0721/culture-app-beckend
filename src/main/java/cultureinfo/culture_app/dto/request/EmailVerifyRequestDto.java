package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
//이메일 코드 인증용 dto
public class EmailVerifyRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String authcode;
}
