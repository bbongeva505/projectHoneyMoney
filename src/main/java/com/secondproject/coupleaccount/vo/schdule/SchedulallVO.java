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

public class SchedulallVO {
    @Schema(description = "시작 날짜" , example = "2020-12-12")
    private LocalDate listStartDate;
    @Schema(description = "마지막 날짜" , example = "2021-01-10")
    private LocalDate listEndDate;
    @Schema(description = "메모" , example = "하와이 여행")
    private String listMemo;
    @Schema(description = "이미지" , example = "ㅁㄴㅇ.jpg")
    private Long listscheduleimg;




}





