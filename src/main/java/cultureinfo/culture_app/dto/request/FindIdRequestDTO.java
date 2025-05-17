package cultureinfo.culture_app.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindIdRequestDTO {
    @NotBlank @Email
    private String email;
    @NotBlank
    private String name;
}
