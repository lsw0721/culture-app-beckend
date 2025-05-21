package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.domain.ContentFavorite;
import cultureinfo.culture_app.dto.response.ContentFavoriteDto;
import cultureinfo.culture_app.repository.ContentDetailRepository;
import cultureinfo.culture_app.repository.ContentFavoriteRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentFavoriteService {
    private final ContentFavoriteRepository contentFavoriteRepository;
    private final SecurityUtil securityUtil;
    private final ContentDetailRepository contentDetailRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ContentFavoriteDto toggleFavorite(Long contentDetailId){
        Long memberId = securityUtil.getCurrentId();
        if(memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다");
        }

        //대상 엔티티 조회
        ContentDetail detail = contentDetailRepository.findById(contentDetailId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 콘텐츠입니다."));
        memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        //기존 찜 여부 확인
        boolean nowFavorited;
        var existing = contentFavoriteRepository
                .findByMemberIdAndContentDetailId(memberId, contentDetailId);
        if(existing.isPresent()){
            //이미 찜되어 있으면 삭제
            contentFavoriteRepository.delete(existing.get());
            detail.decreaseFavoriteCount();
            nowFavorited = false;
        } else {
            //찜이 없으면 새로 추가
            ContentFavorite favorite = ContentFavorite.builder()
                    .member(memberRepository.getReferenceById(memberId))
                    .contentDetail(detail)
                    .build();
            contentFavoriteRepository.save(favorite);
            detail.increaseFavoriteCount();
            nowFavorited = true;
        }

        //결과 반환
        return new ContentFavoriteDto(nowFavorited, detail.getFavoriteCount());
    }

    //특정 콘텐츠에 대해 로그인 사용자가 찜했는지 여부만 조회
    @Transactional(readOnly = true)
    public boolean isFavorite(Long memberId,Long contentDetailId){
        return contentFavoriteRepository.existsByMemberIdAndContentDetailId(memberId, contentDetailId);
    }
}
