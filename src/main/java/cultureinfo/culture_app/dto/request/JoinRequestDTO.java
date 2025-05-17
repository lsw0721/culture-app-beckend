package cultureinfo.culture_app.dto.request;

import cultureinfo.culture_app.domain.type.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class JoinRequestDTO {
    @NotBlank private String username;
    @NotBlank private String password;
    @Email @NotBlank private String email;
    @NotBlank private String name;
    @NotNull private Gender gender;
    @NotBlank private String location;
    @NotBlank private String nickname;
}
