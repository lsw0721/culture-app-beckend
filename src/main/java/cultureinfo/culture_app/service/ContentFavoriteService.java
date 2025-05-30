package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.domain.ContentFavorite;
import cultureinfo.culture_app.dto.response.ContentFavoriteDto;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.ContentDetailRepository;
import cultureinfo.culture_app.repository.ContentFavoriteRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        //대상 엔티티 조회
        ContentDetail detail = contentDetailRepository.findById(contentDetailId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
        memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

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

    //특정 사용자가 찜한 콘텐츠를 불러오는 기능
    @Transactional(readOnly = true)
    public List<ContentDetailDto> isMyFavorite(){
        Long memberId = securityUtil.getCurrentId();
        if(memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        List<ContentDetail> content = contentFavoriteRepository.findAllByMemberId(memberId).stream()
        .map(fav -> fav.getContentDetail())
        .collect(Collectors.toList());
        return ContentDetailDto.fromList(content, true);
    }
}
