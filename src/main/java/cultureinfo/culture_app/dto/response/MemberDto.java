package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
    private final Long id;
    private final String memberId;
    private final String name;
    private final String email;
    private final String location;
    private final String gender;
    private final String nickname;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .location(member.getLocation())
                .gender(member.getGender().name())
                .nickname(member.getNickname())
                .build();
    }
}
