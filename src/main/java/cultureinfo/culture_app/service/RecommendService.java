package cultureinfo.culture_app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cultureinfo.culture_app.dto.response.ContentDetailDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.repository.ContentDetailRepository;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.security.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final SecurityUtil securityUtil;
    private final ContentDetailRepository contentRepository;

    public List<ContentDetailDto> recommend() {

        Long id = securityUtil.getCurrentId();
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("user_id", id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ParameterizedTypeReference<Map<String, List<Long>>> responseType =
        new ParameterizedTypeReference<>() {};

        ResponseEntity<Map<String, List<Long>>> response = restTemplate.exchange("http://localhost:8000/recommend", HttpMethod.POST, entity, responseType);

        List<Long> contentIds = response.getBody().get("recommended_contents");

        List<ContentDetail> contents = contentRepository.findAllById(contentIds);

        Map<Long, ContentDetail> contentMap = contents.stream()
                .collect(Collectors.toMap(ContentDetail::getId, Function.identity()));

        // ID 순서대로 다시 정렬
        List<ContentDetail> orderedContents = contentIds.stream()
                .map(contentMap::get)
                .collect(Collectors.toList());

        return ContentDetailDto.fromList(orderedContents, true); //임시로 true

    }
    
}
