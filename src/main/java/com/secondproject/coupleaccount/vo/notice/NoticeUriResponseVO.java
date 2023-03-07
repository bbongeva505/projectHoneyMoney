package com.secondproject.coupleaccount.vo.notice;

import java.util.List;
import com.secondproject.coupleaccount.entity.NoticeImgInfoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NoticeUriResponseVO {
  @Schema(description = "Uri", example = "http://192.168.0.208:9090/api/notice/image")
  private List<NoticeImgInfoEntity> List;
  @Schema(description = "메시지", example = "등록되었습니다.")
  private String msg;
  @Schema(description = "코드",example ="200")
  private Integer code;
}

