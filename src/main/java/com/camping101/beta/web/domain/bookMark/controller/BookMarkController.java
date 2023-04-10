package com.camping101.beta.web.domain.bookMark.controller;

import com.camping101.beta.web.domain.bookMark.dto.BookMarkCreateRequest;
import com.camping101.beta.web.domain.bookMark.dto.BookMarkCreateResponse;
import com.camping101.beta.web.domain.bookMark.dto.BookMarkListResponse;
import com.camping101.beta.web.domain.bookMark.service.BookMarkService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
@Api(tags = "캠핑 101 - 북마크 API")
public class BookMarkController {
    private final BookMarkService bookMarkService;

    // 북마크 생성
    @PostMapping
    public ResponseEntity<BookMarkCreateResponse> bookMarkAdd(@RequestBody
        BookMarkCreateRequest bookMarkCreateRequest) {

        BookMarkCreateResponse response = bookMarkService.registerBookMark(
            bookMarkCreateRequest);

        return ResponseEntity.ok(response);
    }

    // 회원의 북마크 목록 조회
    // 북마크는 캠프로그를 북마크하는것이기에 캠프로그의 간단한 정보들도 포함되어 있어야 한다.
    @GetMapping("/{memberId}")
    public ResponseEntity<Page<BookMarkListResponse>> bookMarkList(@PathVariable Long memberId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<BookMarkListResponse> bookMarkList = bookMarkService.findBookMarkList(memberId, pageRequest);

        return ResponseEntity.ok(bookMarkList);

    }

    // 북마크 삭제
    @DeleteMapping("/{bookMarkId}")
    public ResponseEntity<?> bookMarkDelete(@PathVariable Long bookMarkId) {

        bookMarkService.removeBookMark(bookMarkId);

        return ResponseEntity.ok("북마크가 삭제되었습니다");

    }


}
