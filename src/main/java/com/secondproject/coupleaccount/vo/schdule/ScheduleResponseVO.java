package com.secondproject.coupleaccount.vo.schdule;

import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResponseVO {
    private Boolean status;
    private String message;
    private List<schduleListVO> scheduleList;
}
