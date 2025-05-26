package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.request.ContentSessionCreateRequestDto;
import cultureinfo.culture_app.dto.request.ContentSessionUpdateRequestDto;
import cultureinfo.culture_app.dto.response.ContentSessionDto;
import cultureinfo.culture_app.service.ContentSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contents/{contentId}/sessions")
@RequiredArgsConstructor
@Validated
public class ContentSessionController {
    private final ContentSessionService sessionService;

    //특정 콘텐츠의 모든 세션 조회
    @GetMapping
    public List<ContentSessionDto> listSessions(@PathVariable Long contentId) {
        return sessionService.getSessionsByContent(contentId);
    }

    //세션 생성
    @PostMapping
    public ResponseEntity<ContentSessionDto> createSession (
            @PathVariable Long contentId,
            @RequestBody @Valid ContentSessionCreateRequestDto dto
    ) {
        dto.setContentDetailId(contentId);
        ContentSessionDto created = sessionService.createSession(dto);
        return ResponseEntity.ok(created);
    }

    //세션 수정
    @PutMapping("/{sessionId}")
    public ContentSessionDto updateSession(
            @PathVariable Long sessionId,
            @RequestBody @Valid ContentSessionUpdateRequestDto dto
    ) {
        return sessionService.updateSession(sessionId, dto);
    }

    //세션 삭제
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long sessionId
    ) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
