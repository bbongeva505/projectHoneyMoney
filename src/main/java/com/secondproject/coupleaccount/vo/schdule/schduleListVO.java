package com.secondproject.coupleaccount.vo.schdule;



import io.swagger.v3.oas.annotations.media.Schema;

  import java.time.LocalDate;

  
  import com.fasterxml.jackson.annotation.JsonFormat;
  


  public interface schduleListVO {
    @Schema(description = "메모 " ,example = "안녕하세요")
    String getMemo();

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "시작 날짜 " ,example = "2020-11-11")
    LocalDate getStdate();
    // @Schema(description = "마지막 날짜 " ,example = "2020-11-11")
    // Date getEnddate();
    @Schema(description = "멤버 seq " ,example = "1")
    Long getMbiseq();
    
    @Schema(description = "스케줄 이미지 " ,example = "스케줄 이미지.jpg")
    String getScheimg();
  
  
    // @Schema(description = "지출 고유번호 " ,example = "1")
    // Long getSeq();
    // // @Schema(description = "지출 카테고리명 " ,example = "카페")
    // // String getCategory();
    // @Schema(description = "지출 액" ,example = "20000")
    // Integer getExpense();
}
