package cultureinfo.culture_app.dto.request;


import cultureinfo.culture_app.domain.type.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class MemberProfileEditRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String currentPassword;
    @Email @NotBlank
    private String email;
    @NotBlank
    private Gender gender;
    @NotBlank
    private String location;
    @NotBlank
    private String nickname;
}