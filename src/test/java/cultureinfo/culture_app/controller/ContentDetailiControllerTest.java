//package cultureinfo.culture_app.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import cultureinfo.culture_app.dto.request.ContentDetailCreateRequestDto;
//import cultureinfo.culture_app.dto.request.ContentDetailUpdateRequestDto;
//import cultureinfo.culture_app.dto.request.ContentSearchRequestDto;
//import cultureinfo.culture_app.dto.response.ContentDetailDto;
//import cultureinfo.culture_app.dto.response.ContentSummaryDto;
//import cultureinfo.culture_app.service.ContentDetailService;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.data.domain.SliceImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.BDDMockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ContentDetailController.class)
//public class ContentDetailiControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private ContentDetailService service;
//
//    @Test
//    @WithMockUser
//    void search_조회_쿼리파라미터없이_성공() throws Exception {
//        // given
//        List<ContentSummaryDto> summaries = List.of(
//                ContentSummaryDto.builder()
//                        .id(1L)
//                        .contentName("콘텐츠1")
//                        .picture("url1")
//                        .startDateTime(LocalDateTime.of(2025,5,20,10,0))
//                        .endDateTime(LocalDateTime.of(2025,5,20,12,0))
//                        .build()
//        );
//        var slice = new SliceImpl<>(summaries, PageRequest.of(0, 20), false);
//        given(service.searchByKeyword(ArgumentMatchers.any(ContentSearchRequestDto.class)))
//                .willReturn(slice);
//
//        // when & then
//        mockMvc.perform(get("/api/contents")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.content[0].id").value(1))
//                .andExpect(jsonPath("$.content[0].contentName").value("콘텐츠1"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void create_생성_POST_성공() throws Exception {
//        // given
//        ContentDetailCreateRequestDto req = new ContentDetailCreateRequestDto(
//                5L,
//                "새콘텐츠",
//                LocalDateTime.of(2025,7,1,14,0),
//                LocalDateTime.of(2025,7,1,16,0),
//                "부산",
//                3000L,
//                null,
//                "가수",
//                "스포츠팀",
//                "브랜드X",
//                "{\"info\":\"test\"}"
//        );
//        ContentDetailDto resp = ContentDetailDto.builder()
//                .id(20L)
//                .contentName("새콘텐츠")
//                .startDateTime(req.getStartDateTime())
//                .endDateTime(req.getEndDateTime())
//                .location("부산")
//                .picture(null)
//                .price(3000L)
//                .isFavorite(false)
//                .favoriteCount(0L)
//                .sessions(List.of())
//                .artistName("가수")
//                .sportTeamName("스포츠팀")
//                .brandName("브랜드X")
//                .detailsJson("{\"info\":\"test\"}")
//                .build();
//        given(service.createContentDetail(ArgumentMatchers.any()))
//                .willReturn(resp);
//
//        // when & then
//        mockMvc.perform(post("/api/contents")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(20))
//                .andExpect(jsonPath("$.contentName").value("새콘텐츠"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void update_PUT_성공() throws Exception {
//        // given
//        ContentDetailUpdateRequestDto req = new ContentDetailUpdateRequestDto(
//                null,
//                "업데이트콘텐츠",
//                LocalDateTime.of(2025,8,1,10,0),
//                LocalDateTime.of(2025,8,1,12,0),
//                null,
//                null,
//                "대구",
//                null,
//                "가수2",
//                "팀2",
//                "브랜드2",
//                "{\"upd\":\"yes\"}"
//        );
//        ContentDetailDto resp = ContentDetailDto.builder()
//                .id(30L)
//                .contentName("업데이트콘텐츠")
//                .startDateTime(req.getStartDateTime())
//                .endDateTime(req.getEndDateTime())
//                .location("대구")
//                .picture(null)
//                .price(0L)
//                .isFavorite(false)
//                .favoriteCount(0L)
//                .sessions(List.of())
//                .artistName("가수2")
//                .sportTeamName("팀2")
//                .brandName("브랜드2")
//                .detailsJson("{\"upd\":\"yes\"}")
//                .build();
//        given(service.updateContentDetail(eq(30L), ArgumentMatchers.any()))
//                .willReturn(resp);
//
//        // when & then
//        mockMvc.perform(put("/api/contents/30")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(30))
//                .andExpect(jsonPath("$.location").value("대구"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void delete_DELETE_성공() throws Exception {
//        // service.deleteContentDetail()는 void
//        willDoNothing().given(service).deleteContentDetail(40L);
//
//        // when & then
//        mockMvc.perform(delete("/api/contents/40").with(csrf()))
//                .andExpect(status().isNoContent());
//    }
//}
