package cultureinfo.culture_app.dto.request;


import cultureinfo.culture_app.domain.type.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class MemberProfileEditRequestDto {
    @NotBlank
    private String name;
    @Email @NotBlank
    private String email;
    @NotNull
    private Gender gender;
    @NotBlank
    private String location;
    @NotBlank
    private String nickname;
}
