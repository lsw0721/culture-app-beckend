package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.MemberRequestDto;
import cultureinfo.culture_app.dto.response.MemberDto;
import cultureinfo.culture_app.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //사용자 조회
    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        return MemberDto.from(member);
    }

    //사용자 정보 업데이트
    public void updateMember(Long id, MemberRequestDto request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        member.update(request.getName(), request.getNickname(), request.getLocation());
    }
}
