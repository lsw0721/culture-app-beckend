package cultureinfo.culture_app.controller;


import cultureinfo.culture_app.dto.request.*;
import cultureinfo.culture_app.dto.response.MemberDto;
import cultureinfo.culture_app.dto.security.JwtToken;
import cultureinfo.culture_app.security.JwtTokenProvider;
import cultureinfo.culture_app.service.EmailService;
import cultureinfo.culture_app.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/members")
public class
MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    // --- 회원가입 및 로그임 ---

    //회원가입 인증용 이메일 전송
    @PostMapping("/join/send-email")
    public ResponseEntity<Void> sendJoinEmail(@RequestParam String email) {
        emailService.sendJoinEmail(email);
        return ResponseEntity.noContent().build();
    }

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody JoinRequestDTO req) {
        String username = memberService.join(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(username);
    }

    //키워드 저장
    @PatchMapping("/keywords")
    public ResponseEntity<Void> updateKeywords(@Valid @RequestBody UpdateKeywordRequestDto req) {
        memberService.updateKeyword(req);
        return ResponseEntity.noContent().build();
    }

    //로그인
    @PostMapping("/sign-in")
    public ResponseEntity<JwtToken> signIn(@Valid @RequestBody LoginRequestDTO req){
        JwtToken token = memberService.signIn(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(token);
    }

    //이메일로 아이디 찾기
    @PostMapping("/find-username")
    public ResponseEntity<String> findUsernameByEmail(@Valid @RequestBody FindIdRequestDTO req){
        String username = memberService.findUsernameByEmail(req);
        return ResponseEntity.ok(username);
    }

    //비밀번호 찾기(임시 비밀번호 사용)
    @PostMapping("/find-password/temp")
    public ResponseEntity<Void> sendPasswordEmail(@Valid @RequestBody TempPasswordRequestDto req) {
        emailService.sendTemporaryPassword(req.getUsername(), req.getEmail());
        return ResponseEntity.noContent().build();
    }

    //---개인정보 수정 로직---

    //프로필 조회 전 비밀번호 검증
    @PostMapping("/profile/verify")
    public ResponseEntity<Void> verifyProfilePassword(@Valid @RequestBody VerifyPasswordRequestDto req){
        memberService.verifyCurrentPassword(req.getPassword());
        return ResponseEntity.ok().build();
    }

    //프로필 확인(내 정보 조회) -> 수정 필요
    @GetMapping("/profile/me")
    public ResponseEntity<MemberDto> getCurrentProfile() {
        MemberDto dto = memberService.getCurrentMember();
        return ResponseEntity.ok(dto);
    }

    // 8) 프로필 변경
    @PutMapping("/profile/update-profile")
    public ResponseEntity<MemberDto> updateProfile(
            @Valid @RequestBody MemberProfileEditRequestDto req
    ) {
        MemberDto updated = memberService.updateProfile(req);
        return ResponseEntity.ok(updated);
    }

    // 9) 비밀번호 변경
    @PutMapping("/profile/update-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody UpdatePasswordRequestDto req
    ) {
        memberService.updatePassword(req);
        return ResponseEntity.noContent().build();
    }

    // --- JWT 토큰 재발급 ---

    // 10) 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<JwtToken> reissueToken(@Valid @RequestBody TokenRequestDto req) // --- 토큰 재발급 ---
    {
        try {
            JwtToken newTokens = jwtTokenProvider.reissueAccessToken(req);
            return ResponseEntity.ok(newTokens);
        } catch (RuntimeException e) {
            // 실패 시 401 Unauthorized
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

    }
}
