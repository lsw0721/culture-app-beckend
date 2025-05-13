package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.request.MemberRequestDto;
import cultureinfo.culture_app.dto.response.MemberDto;
import cultureinfo.culture_app.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
public class MemberController {
    private final MemberService memberService;
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable Long id,
                                             @RequestBody MemberRequestDto request) {
        memberService.updateMember(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }
}
