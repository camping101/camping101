package com.camping101.beta.bookMark.service;

import com.camping101.beta.bookMark.dto.BookMarkCreateRequest;
import com.camping101.beta.bookMark.dto.BookMarkCreateResponse;
import com.camping101.beta.bookMark.dto.BookMarkListResponse;
import com.camping101.beta.bookMark.entity.BookMark;
import com.camping101.beta.bookMark.repository.BookMarkRepository;
import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.campLog.repository.CampLogRepository;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final CampLogRepository campLogRepository;
    private final MemberRepository memberRepository;

    // 북마크 생성
    public BookMarkCreateResponse registerBookMark(BookMarkCreateRequest bookMarkCreateRequest) {

        CampLog findCampLog = campLogRepository.findById(bookMarkCreateRequest.getCampLogId())
            .orElseThrow(() -> {
                throw new RuntimeException("존재하는 캠프로그가 없습니다");
            });

        Member findMember = memberRepository.findById(bookMarkCreateRequest.getMemberId())
            .orElseThrow(() -> {
                throw new RuntimeException("존재하는 회원이 없습니다");
            });

        BookMark bookMark = new BookMark();

        bookMarkRepository.save(bookMark);

        bookMark.addCampLog(findCampLog);
        bookMark.changeMember(findMember);

        return BookMark.toBookMarkCreateResponse(bookMark);

    }

    // 북마크 목록 조회
    @Transactional(readOnly = true)
    public Page<BookMarkListResponse> findBookMarkList(Long memberId, Pageable pageable) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 회원이 없습니다");
        });

        Page<BookMark> bookMarkList = bookMarkRepository.findBookMarkByMember(findMember, pageable);

        return bookMarkList.map(BookMark::toBookMarkListResponse);

    }

    // 북마크 삭제
    public void removeBookMark(Long bookmarkId) {

        BookMark findBookMark = bookMarkRepository.findById(bookmarkId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 북마크가 없습니다.");
        });

        bookMarkRepository.delete(findBookMark);

    }
}
