package com.secondproject.coupleaccount.vo.response;

import com.secondproject.coupleaccount.vo.MyAccountInfoVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountBookResponseVO {
    private Boolean status;
    private String message;
    private MyAccountInfoVO myAccountInfoVO;
}
