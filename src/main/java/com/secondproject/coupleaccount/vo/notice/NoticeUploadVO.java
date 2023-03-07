package com.secondproject.coupleaccount.vo.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeUploadVO {
  @Schema(description = "메시지", example = "등록되었습니다.")
  private String msg;
  @Schema(description = "코드",example ="200")
  private Integer code;
}
