package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.domain.type.Role;
import cultureinfo.culture_app.dto.request.*;
import cultureinfo.culture_app.dto.response.MemberDto;
import cultureinfo.culture_app.dto.security.JwtToken;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.JwtTokenProvider;
import cultureinfo.culture_app.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    //회원가입
    @Transactional
    public String join(JoinRequestDTO joinRequestDTO) {

        if (memberRepository.existsByUsername(joinRequestDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if (memberRepository.existsByEmail(joinRequestDTO.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(joinRequestDTO.getPassword());

        Member member = Member.builder()
                .name(joinRequestDTO.getName())
                .username(joinRequestDTO.getUsername())
                .password(encodedPassword)
                .email(joinRequestDTO.getEmail())
                .location(joinRequestDTO.getLocation())
                .gender(joinRequestDTO.getGender())
                .nickname(joinRequestDTO.getNickname())
                .roles(Set.of(Role.ROLE_USER))
                .build();
        memberRepository.save(member);
        return member.getUsername();
    }

    //로그인
    @Transactional
    public JwtToken signIn(String username, String password) {
        try{
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 유저가 없습니다."));
            // 입력된 비밀번호와 해시된 비밀번호 비교
            if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
            // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

            return jwtToken;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //단건 조회

    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        return MemberDto.from(member);
    }

    //키워드 저장
    @Transactional
    public void updateKeyword(UpdateKeywordRequestDto req) {
        Long userId = securityUtil.getCurrentId();
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        if(req.getKeyword1() != null)
            member.updateKeyword1(req.getKeyword1());
        if(req.getKeyword2() != null)
            member.updateKeyword2(req.getKeyword2());
        if(req.getKeyword3() != null)
            member.updateKeyword3(req.getKeyword3());
    }

    //이메일 + 이름으로 아이디 찾기
    @Transactional(readOnly = true)
    public String findUsernameByEmail(FindIdRequestDTO req) {
        return memberRepository.findByEmail(req.getEmail())
                .filter(member -> member.getName().equals(req.getName()))
                .map(Member::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다"));
    }

    //아이디 + 이메일로 본인 인증 후 비밀번호 변경

    //임시 비밀번호 저장
    @Transactional
    public void saveTemporaryPassword(String username, String email, String tempPassword) {
        Member member = memberRepository.findByUsername(username)
                .filter(m ->  m.getEmail().equals(email))
                .orElseThrow(() ->
                        new IllegalArgumentException("아이디와 이메일이 일치하지 않습니다.")
                );
        String encoded = passwordEncoder.encode(tempPassword);
        member.changePassword(encoded);
    }

    //username + email 검증 수행
    @Transactional(readOnly = true)
    public void validateUsernameAndEmail(String username, String email) {
        memberRepository.findByUsername(username)
                .filter(m -> m.getEmail().equals(email))
                .orElseThrow(() ->
                        new IllegalArgumentException("아이디와 이메일이 일치하지 않습니다."));
    }

    //개인정보(프로필, 비밀번호) 수정을 하려면 비밀번호를 입력해야 함
    //프로필 수정
    //비밀번호 변경

    //비밀번호 검증
    @Transactional(readOnly = true)
    public void verifyCurrentPassword(String password) {
        Long userId = securityUtil.getCurrentId();
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
    }

    //현재 로그인 사용자 정보 조회
    @Transactional(readOnly = true)
    public MemberDto getCurrentMember() {
        Long userId = securityUtil.getCurrentId();
        Member m = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        return MemberDto.from(m);
    }

    //프로필 변경
    @Transactional
    public MemberDto updateProfile(MemberProfileEditRequestDto req) {
        verifyCurrentPassword(req.getCurrentPassword());

        Long userId = securityUtil.getCurrentId();
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        member.update(req.getName(), req.getNickname(), req.getLocation(), req.getGender(), req.getEmail());
        return MemberDto.from(member);
    }

    //비밀번호 변경
    @Transactional
    public void updatePassword(UpdatePasswordRequestDto req) {
        Long userId = securityUtil.getCurrentId();
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        if (!bCryptPasswordEncoder.matches(req.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        String newPassword = passwordEncoder.encode(req.getNewPassword());
        member.changePassword(newPassword);
    }

    //id로 이메일 가져오기
    @Transactional
    public String getEmailbyId() {
        Long userId = securityUtil.getCurrentId();
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        return member.getEmail();
    }

}
