package com.secondproject.coupleaccount.vo.member;

import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.vo.MemberJoinVo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberJoinResponseVO {
    private boolean status;
    private String message;
    private String userEmail;

    public MemberJoinResponseVO(boolean status, MemberJoinVo memberJoinVo, String message) {
        this.status = status;
        this.message = message;
        this.userEmail = memberJoinVo.getMbiBasicEmail();
    }
}
