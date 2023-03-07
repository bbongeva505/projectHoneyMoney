package com.secondproject.coupleaccount.vo;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ShareAccountJoinVO {  
    @Schema(description = "공유통장 이름", example = "우리 통장")  
    private String name;
    @Schema(description = "기념일(사귄날)", example = "2021-05-02")
    private LocalDate startDay;        
}
