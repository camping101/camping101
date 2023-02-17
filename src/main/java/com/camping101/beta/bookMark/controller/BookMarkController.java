package com.camping101.beta.bookMark.controller;

import com.camping101.beta.bookMark.dto.BookMarkCreateRequest;
import com.camping101.beta.bookMark.dto.BookMarkCreateResponse;
import com.camping101.beta.bookMark.dto.BookMarkListResponse;
import com.camping101.beta.bookMark.service.BookMarkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    // 북마크 생성
    public ResponseEntity<BookMarkCreateResponse> bookMarkAdd(BookMarkCreateRequest bookMarkCreateRequest) {

        BookMarkCreateResponse response = bookMarkService.registerBookMark(
            bookMarkCreateRequest);

        return ResponseEntity.ok(response);

    }

    // 북마크 목록 조회
    public ResponseEntity<List<BookMarkListResponse>> bookMarkList(@RequestParam Long memberId) {

        List<BookMarkListResponse> bookMarkList = bookMarkService.findBookMarkList(memberId);

        return ResponseEntity.ok(bookMarkList);

    }

    // 북마크 삭제
    public ResponseEntity<?> bookMarkDelete(@RequestParam Long bookMarkId) {


        bookMarkService.removeBookMark(bookMarkId);

        return ResponseEntity.ok("북마크가 삭제되었습니다");

    }




}
