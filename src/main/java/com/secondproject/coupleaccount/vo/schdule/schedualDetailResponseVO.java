package com.secondproject.coupleaccount.vo.schdule;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.secondproject.coupleaccount.entity.ScheduleImgInfoEntity;
import com.secondproject.coupleaccount.entity.ScheduleInfoEntity;

import lombok.Builder;
import lombok.Data;

@Data
public class schedualDetailResponseVO {
private Long siSeq;
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
private LocalDate stdate;
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
private LocalDate eddate;
private String memo;
private String scheimg;

public schedualDetailResponseVO(ScheduleInfoEntity infoEntity, ScheduleImgInfoEntity imgInfoEntity)
{
    this.siSeq = infoEntity.getSiSeq();
    this.stdate = infoEntity.getSiStartDate();
    this.eddate = infoEntity.getSiEndDate();
    this.memo = infoEntity.getSiMemo();

    if(imgInfoEntity != null) {
        this.scheimg = imgInfoEntity.getSiiUri();
    }
    
}


}
