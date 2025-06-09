package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TempPasswordRequestDto {
    @NotBlank @Email
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String name;
}
