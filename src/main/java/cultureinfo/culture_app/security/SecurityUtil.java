package cultureinfo.culture_app.security;

import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final MemberRepository memberRepository;

    public Long getCurrentId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication information.");
        }
        Member user = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를를 가진 유저가 없습니다."));
        return user.getId();
    }

    public String getCurrentUsername(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication information.");
        }
        return authentication.getName();
    }
}
