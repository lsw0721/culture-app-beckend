package cultureinfo.culture_app.controller;


import cultureinfo.culture_app.ai.AiRunner;
import cultureinfo.culture_app.dto.request.*;
import cultureinfo.culture_app.dto.response.ArticleDto;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.dto.response.MemberDto;
import cultureinfo.culture_app.dto.security.JwtToken;
import cultureinfo.culture_app.security.JwtTokenProvider;
import cultureinfo.culture_app.service.AnnouncementService;
import cultureinfo.culture_app.service.ArticleService;
import cultureinfo.culture_app.service.ContentFavoriteService;
import cultureinfo.culture_app.service.EmailService;
import cultureinfo.culture_app.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
    private final ArticleService articleService;
    private final AnnouncementService announcementService;
    private final ContentFavoriteService contentFavoriteService;
    private final AiRunner aiRunner;

    // --- 회원가입 및 로그인 ---

    //회원가입 인증용 이메일 전송
    //이메일 형식 틀리면 400에러
    @PostMapping("/join/send-email")
    public ResponseEntity<Void> sendJoinEmail(@Valid @RequestBody JoinEmailRequestDto email) {

            emailService.sendJoinEmail(email.getEmail());
            return ResponseEntity.noContent().build(); //204
    }


    //회원가입
    //400 에러는 email 형식에 맞지 않거나, null 값을 받았을 때, 그럴 때 발생
    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody JoinRequestDTO req) {
        emailService.checkEmailVerified(req.getEmail());
        String username = memberService.join(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(username); //201
    }

    //아이디 중복 검증
    @PostMapping("/join/verify-username")
    public ResponseEntity<Void> verifyUsername(@Valid @RequestBody JoinUsernameRequestDto username){
        memberService.verifyUsername(username.getUsername());
        return ResponseEntity.ok().build();
    }

    //닉네임 중복 검증
    @PostMapping("/join/verify-nickname")
    public ResponseEntity<Void> verifyNickname(@Valid @RequestBody JoinNicknameRequestDto nickname){
        memberService.verifyNickname(nickname.getNickname());
        return ResponseEntity.ok().build();
    }

    //이메일 코드 인증
    @PostMapping("join/verify-emailcode")
    public ResponseEntity<Void> verifyEmailCode(@RequestBody EmailVerifyRequestDto req) {
        emailService.verifyAuthCode(req.getEmail(), req.getAuthcode());
        return ResponseEntity.ok().build();
    }

    //키워드 저장
    //400 에러는 형식에 맞지 않거나, null 값을 받았을 때, 그럴 때 발생
    @PatchMapping("/keywords")
    public ResponseEntity<Void> updateKeywords(@Valid @RequestBody UpdateKeywordRequestDto req) {
        memberService.updateKeyword(req);
        aiRunner.runInBackground();
        return ResponseEntity.noContent().build();
    }

    //로그인
    @PostMapping("/sign-in")
    public ResponseEntity<JwtToken> signIn(@Valid @RequestBody LoginRequestDTO req){
        JwtToken token = memberService.signIn(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(token);

    }

    //이메일로 아이디 찾기
    //400 에러는 이메일 형식이 안 맞거나 null일 때
    @PostMapping("/find-username")
    public ResponseEntity<String> findUsernameByEmail(@Valid @RequestBody FindIdRequestDTO req){
        String username = memberService.findUsernameByEmail(req);
        return ResponseEntity.ok(username);
    }

    //비밀번호 찾기(임시 비밀번호 사용)
    @PostMapping("/find-password/temp")
    public ResponseEntity<Void> sendPasswordEmail(@Valid @RequestBody TempPasswordRequestDto req) {
        emailService.sendTemporaryPassword(req.getUsername(), req.getEmail(), req.getName());
        return ResponseEntity.noContent().build(); //204
    
    }

    //---개인정보 수정 로직---

    //프로필 조회 전 비밀번호 검증
    //400 입력을 안하면
    @PostMapping("/profile/verify")
    public ResponseEntity<Void> verifyProfilePassword(@Valid @RequestBody VerifyPasswordRequestDto req){
        memberService.verifyCurrentPassword(req.getPassword());
        return ResponseEntity.ok().build(); //200
    }

    //프로필 확인(내 정보 조회) -> 수정 필요
    @GetMapping("/profile/me")
    public ResponseEntity<MemberDto> getCurrentProfile() {
        MemberDto dto = memberService.getCurrentMember();
        return ResponseEntity.ok(dto);
    }

    // 8) 프로필 변경
    //400은 이메일 형식 그런 거 안 지켰을 때 발생
    @PutMapping("/profile/me")
    public ResponseEntity<MemberDto> updateProfile(@Valid
             @RequestBody MemberProfileEditRequestDto req
    ) {
        MemberDto updated = memberService.updateProfile(req);
        return ResponseEntity.ok(updated);
    }

    //프로필용 닉네임 중복 검증
    @PostMapping("/profile/verify-nickname")
    public ResponseEntity<Void> verifyMyNickname(@Valid @RequestBody JoinNicknameRequestDto nickname){
        memberService.verifyMyNickname(nickname.getNickname());
        return ResponseEntity.ok().build();
    }

    // 9) 비밀번호 변경
    @PutMapping("/profile/update-password")
    public ResponseEntity<Void> changePassword(@Valid 
           @RequestBody UpdatePasswordRequestDto req
    ) {
        memberService.updatePassword(req);
        return ResponseEntity.noContent().build();
    }

    // 자신이 쓴 게시글 확인
    @GetMapping("/profile/myarticle")
    public ResponseEntity<List<ArticleDto>> getMyArticles() {
        List<ArticleDto> articles = articleService.getMyArticles();
        return ResponseEntity.ok(articles);
    }

    // 자신이 찜한 컨텐츠 확인
    @GetMapping("/profile/mycontent")
    public ResponseEntity<List<ContentDetailDto>> getMyContents() {
        List<ContentDetailDto> contents = contentFavoriteService.isMyFavorite();
        return ResponseEntity.ok(contents);
    }

    //------------------공지사항--------------------
    //공지사항 전체 조회
    @GetMapping("/profile/announcement")
    public ResponseEntity<List<ArticleDto>> getAllAnnouncements() {
        List<ArticleDto> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(announcements);
    }

    //공지사항 단건 조회
    @GetMapping("/profile/announcement/{id}")
    public ResponseEntity<ArticleDto> getAnnouncement(@PathVariable Long id) {
        ArticleDto announcement = announcementService.getAnnouncement(id);
        return ResponseEntity.ok(announcement);
    }

    //공지사항 생성
    @PostMapping("/profile/announcement")
    public ResponseEntity<ArticleDto> createAnnouncement(
            @RequestBody AnnouncementRequestDto requestDto) {
        ArticleDto created = announcementService.createAnnouncement(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created); //201
    }

    // 공지사항 수정
    @PutMapping("/profile/announcement/{id}")
    public ResponseEntity<ArticleDto> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody AnnouncementUpdateRequestDto requestDto) {
        ArticleDto updated = announcementService.updateAnnouncement(id, requestDto);
        return ResponseEntity.ok(updated); //200
    }

    // 공지사항 삭제
    @DeleteMapping("/profile/announcement/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build(); //204

    }

    //1:1 이메일 문의
    //String으로 문의 제목, 내용 받아서 등록된 메일로 자기자신에게 보내기
    //메일에 사용자 메일도 같이 첨부해서 보내기(관리자가 문의내용에 대한 답변을 사용자에게 개인적으로 보낼 수 있도록)
    @PostMapping("/profile/inquiry")
    public ResponseEntity<Void> inquiryEmail(
            @RequestBody InquiryRequestDto requestDto) {
        emailService.sendInquiry(requestDto);
        return ResponseEntity.noContent().build(); //204
    }

    // --- JWT 토큰 재발급 ---

    // 10) 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<JwtToken> reissueToken(@Valid @RequestBody TokenRequestDto req) // --- 토큰 재발급 ---
    {
        JwtToken newTokens = jwtTokenProvider.reissueAccessToken(req);
        return ResponseEntity.ok(newTokens);

    }
}
