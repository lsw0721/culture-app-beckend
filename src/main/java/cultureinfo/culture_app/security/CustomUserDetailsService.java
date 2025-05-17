package cultureinfo.culture_app.security;

import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username)
                );
        // member.getRoles()는 Set<Role>이고, Role은 GrantedAuthority 구현체
        return new CustomUserDetails(member, member.getRoles());
    }
}
