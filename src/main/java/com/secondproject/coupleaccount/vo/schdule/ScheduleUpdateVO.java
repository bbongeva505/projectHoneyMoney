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
public class ScheduleUpdateVO {
    @Schema(description = "수정 시작 날짜" , example = "2021-12-12")
    private LocalDate UpdateStartDate;
    @Schema(description = " 수정 마지막 날짜" , example = "2022-01-10")
    private LocalDate UpdateEndDate;
    @Schema(description = "수정 후 메모" , example = "1년후 하와이 여행")
    private String siMemo;
}

