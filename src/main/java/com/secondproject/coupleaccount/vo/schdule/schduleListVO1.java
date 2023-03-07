package com.secondproject.coupleaccount.vo.schdule;



import io.swagger.v3.oas.annotations.media.Schema;

  import java.time.LocalDate;

  
  import com.fasterxml.jackson.annotation.JsonFormat;
  


  public interface schduleListVO1 {
    @Schema(description = "메모 " ,example = "안녕하세요")
    String getMemo();

    @Schema(description = "캘린더 일정 기본키", example = "1")
    Long getSiseq();
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "시작 날짜 " ,example = "2020-11-11")
    LocalDate getStdate();
    // @Schema(description = "마지막 날짜 " ,example = "2020-11-11")
    // Date getEnddate();
    // @Schema(description = "멤버 seq " ,example = "1")
    // Long getMbiseq();
  }
