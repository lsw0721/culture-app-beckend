package cultureinfo.culture_app.controller;


import cultureinfo.culture_app.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cultureinfo.culture_app.dto.response.ContentDetailDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {
    private final RecommendService recommendService;
    //게시글 생성
    @PostMapping
    public ResponseEntity<List<ContentDetailDto>> recommendContents() {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recommendService.recommend());
    }
}
