package com.secondproject.coupleaccount.vo.schdule;

import java.time.LocalDate;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchedulInfoVO {
    @Schema(description = "시작 날짜" , example = "2020-12-12")
    private LocalDate siStartDate;
    @Schema(description = "마지막 날짜" , example = "2021-01-10")
    private LocalDate siEndDate;
    @Schema(description = "메모" , example = "하와이 여행")
    private String siMemo;

}
  