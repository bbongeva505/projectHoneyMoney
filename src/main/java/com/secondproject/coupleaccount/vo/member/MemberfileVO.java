package com.secondproject.coupleaccount.vo.member;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberfileVO {
    @Schema(description = "이미지 파일", example = "1234.jpg")
    private MultipartFile file;
}
