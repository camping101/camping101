package com.camping101.beta.bookMark.service;

import static com.camping101.beta.member.entity.status.MemberStatus.IN_USE;
import static com.camping101.beta.member.entity.type.MemberType.CUSTOMER;
import static com.camping101.beta.member.entity.type.SignInType.EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.camping101.beta.bookMark.dto.BookMarkCreateRequest;
import com.camping101.beta.bookMark.dto.BookMarkCreateResponse;
import com.camping101.beta.bookMark.dto.BookMarkListResponse;
import com.camping101.beta.bookMark.entity.BookMark;
import com.camping101.beta.bookMark.repository.BookMarkRepository;
import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.campLog.repository.CampLogRepository;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class BookMarkServiceTest {

    @InjectMocks
    private BookMarkService bookMarkService;

    @Mock
    private BookMarkRepository bookMarkRepository;
    @Mock
    private CampLogRepository campLogRepository;
    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void createBookMarkServiceTest() {
        //when
        BookMarkService bookMarkService = new BookMarkService(bookMarkRepository, campLogRepository,
            memberRepository);

        //then
        assertNotNull(bookMarkService);
    }


    // 북마크 생성
    @DisplayName("북마크 생성 성공")
    @Test
    void registerBookMark() {
        //given

        BookMarkCreateRequest request = createBookMarkCreateRequest();

        Member member = createMember(request.getMemberId());

        CampLog campLog = createCampLog(member, request.getCampLogId(), request.getCampLogName());

        BookMark bookMark = createBookMark(member, campLog, request.getBookMarkId());

        given(campLogRepository.findById(request.getBookMarkId())).willReturn(Optional.of(campLog));
        given(memberRepository.findById(request.getMemberId())).willReturn(Optional.of(member));

        BookMarkCreateResponse response = createBookMarkCreateResponse();

        //when

        bookMarkService.registerBookMark(createBookMarkCreateRequest());
        //then

        assertThat(bookMark.getBookMarkId()).isEqualTo(response.getBookMarkId());
        assertThat(bookMark.getMember().getMemberId()).isEqualTo(response.getMemberId());
        assertThat(bookMark.getCampLog().getCampLogId()).isEqualTo(response.getCampLogId());
        assertThat(bookMark.getCampLog().getCampLogName()).isEqualTo(response.getCampLogName());
    }

    private BookMark createBookMark(Member member, CampLog campLog, Long bookMarkId) {
        return BookMark.builder()
            .bookMarkId(bookMarkId)
            .member(member)
            .campLog(campLog)
            .build();
    }

    private CampLog createCampLog(Member member, Long campLogId, String campLogName) {
        return CampLog.builder()
            .campLogId(campLogId)
            .campLogName(campLogName)
            .member(member)
            .build();
    }

    private Member createMember(Long memberId) {
        return Member.builder()
            .memberId(memberId)
            .nickName("suchan")
            .email("suchan@naver.com")
            .password("suchan1234")
            .phoneNumber("010-1234-5678")
            .signInType(EMAIL)
            .memberType(CUSTOMER)
            .memberStatus(IN_USE)
            .image("aaa")
            .build();
    }

    private BookMarkCreateRequest createBookMarkCreateRequest() {

        return BookMarkCreateRequest.builder()
            .bookMarkId(1L)
            .memberId(1L)
            .campLogId(1L)
            .campLogName("캠핑장1")
            .build();

    }

    private BookMarkCreateResponse createBookMarkCreateResponse() {

        return BookMarkCreateResponse.builder()
            .bookMarkId(1L)
            .memberId(1L)
            .campLogId(1L)
            .campLogName("캠핑장1")
            .build();
    }


    // 북마크 목록 조회
    @DisplayName("북마크 목록 조회 성공")
    @Test
    void findBookMarkList() {

//        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
//            throw new RuntimeException("존재하는 회원이 없습니다");
//        });
//
//        Page<BookMark> bookMarkList = bookMarkRepository.findBookMarkByMember(findMember, pageable);
//
//        return bookMarkList.map(BookMark::toBookMarkListResponse);
//
//        private Long bookMarkId;
//        private Long memberId;
//        private String nickName;
//
//        private Long campLogId;
//        private String campLogName;
//        private String title;
//        private String description;
//        private String image;

        //given
        Long memberId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 5);
        Member member = createMember(memberId);

        BookMark bookMark1 = createBookMark(member);
        BookMark bookMark2 = createBookMark(member);
        BookMark bookMark3 = createBookMark(member);
        BookMark bookMark4 = createBookMark(member);
        BookMark bookMark5 = createBookMark(member);
        BookMark bookMark6 = createBookMark(member);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        Page<BookMark> pagedBookMark = bookMarkRepository.findBookMarkByMember(member,
            pageRequest);

        //when
        bookMarkRepository.save(bookMark1);
        bookMarkRepository.save(bookMark2);
        bookMarkRepository.save(bookMark3);
        bookMarkRepository.save(bookMark4);
        bookMarkRepository.save(bookMark5);
        bookMarkRepository.save(bookMark6);

        Page<BookMarkListResponse> bookMarkList = bookMarkService.findBookMarkList(memberId,
            pageRequest);
        //then

        assertThat(bookMarkList.getSize()).isEqualTo(5);
    }

    private BookMark createBookMark(Member member) {
        return BookMark.builder()
            .member(member)
            .build();
    }

    // 북마크 삭제
    @DisplayName("북마크 삭제 성공")
    @Test
    void removeBookMark() {
        //given
        //when
        //then
    }


}