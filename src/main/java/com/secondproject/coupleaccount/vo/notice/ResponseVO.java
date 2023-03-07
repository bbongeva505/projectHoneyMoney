package com.secondproject.coupleaccount.vo.notice;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.secondproject.coupleaccount.entity.NoticeImgInfoEntity;
import com.secondproject.coupleaccount.entity.NoticeInfoEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseVO {
  @Schema(description = "공지사항 번호")
  private Long noticeNo;
  @Schema(description = "공지사항 내용")
  private String memo;
  @Schema(description = "공지사항 uri")
  private String uri;
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "공지사항 등록일")
  private LocalDate date;
  @Schema(description = "메시지", example = "등록되었습니다.")
  private String msg;
  @Schema(description = "코드",example ="200")
  private Integer code;
}