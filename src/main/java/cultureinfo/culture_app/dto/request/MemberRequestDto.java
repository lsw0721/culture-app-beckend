package cultureinfo.culture_app.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    private String name;
    private String nickname;
    private String location;
}
