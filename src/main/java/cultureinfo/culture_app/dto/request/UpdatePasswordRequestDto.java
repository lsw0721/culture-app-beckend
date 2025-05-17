package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

//개인정보 수정 -> 비밀번호 변경
public class UpdatePasswordRequestDto {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;
}
