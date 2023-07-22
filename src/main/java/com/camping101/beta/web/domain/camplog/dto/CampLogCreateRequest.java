package com.camping101.beta.web.domain.camplog.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampLogCreateRequest {

    private String writerEmail;
    private Long siteId;
    private LocalDateTime visitedAt;
    private String visitedWith;
    private List<Long> recTags;
    private String title;
    private String description;

    private MultipartFile image;
    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;
    private MultipartFile image4;
    private MultipartFile image5;

}
