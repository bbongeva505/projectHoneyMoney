package com.secondproject.coupleaccount.vo.notice;

import java.util.List;

// import com.secondproject.coupleaccount.entity.NoticeAddEntity;
import com.secondproject.coupleaccount.entity.NoticeInfoEntity;
// import com.secondproject.coupleaccount.entity.NoticeListEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeResponseVO {
  // @Schema(description = "상태")
  // private boolean status;
  // @Schema(description = "메시지")
  // private String msg; 
  @Schema(description = "공지사항 리스트")
  private List<NoticeInfoEntity> List;
  @Schema(description = "총 공지사항 수", example = "2")
  private Long total;
  @Schema(description = "총 페이지 수", example="5")
  private Integer totalPage;
  @Schema(description = "현재 페이지", example="(1페이지 -> 0/2페이지 ->1")
  private Integer currentPage;
  // @Schema(description = "상태", example = "true")
  // private Boolean status;
  @Schema(description = "메시지", example = "등록되었습니다.")
  private String msg;
  @Schema(description = "코드",example ="200")
  private Integer code;


}
