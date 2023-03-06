package com.camping101.beta.campLog.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> recTags;
    private String title;
    private String description;

    private MultipartFile image;
    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;
    private MultipartFile image4;
    private MultipartFile image5;

}
