package com.secondproject.coupleaccount.vo.schdule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleImgInfoVo {
    private boolean status;
    private String message;
}

