package com.camping101.beta.web.domain.bookmark.service;

import com.camping101.beta.db.entity.bookmark.BookMark;
import com.camping101.beta.db.entity.camplog.CampLog;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.web.domain.bookmark.dto.CreateBookMarkRq;
import com.camping101.beta.web.domain.bookmark.dto.CreateBookMarkRs;
import com.camping101.beta.web.domain.bookmark.repository.BookMarkRepository;
import com.camping101.beta.web.domain.camplog.service.FindCampLogService;
import com.camping101.beta.web.domain.member.service.FindMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final FindCampLogService findCampLogService;
    private final FindMemberService findMemberService;
    private final FindBookMarkService findBookMarkService;

    // 북마크 생성
    public CreateBookMarkRs registerBookMark(CreateBookMarkRq rq) {

        CampLog findCampLog = findCampLogService.findCampLogOrElseThrow(rq.getCampLogId());

        Member findMember = findMemberService.findMemberOrElseThrow(rq.getMemberId());

        BookMark bookMark = new BookMark();

        bookMarkRepository.save(bookMark);

        bookMark.changeCampLog(findCampLog);
        bookMark.changeMember(findMember);

        return CreateBookMarkRs.createBookMarkRs(bookMark);
    }


    // 북마크 삭제
    public void removeBookMark(Long bookmarkId) {

        BookMark findBookMark = findBookMarkService.findBookMarkOrElseThrow(bookmarkId);

        bookMarkRepository.delete(findBookMark);

    }
}
