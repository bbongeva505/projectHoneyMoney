package com.secondproject.coupleaccount.vo.statistic;



import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberCateVO {
    private Long seq;
    public MemberCateVO(MemberBasicInfoEntity data){
        this.seq = data.getMbiSeq();
    }
}
