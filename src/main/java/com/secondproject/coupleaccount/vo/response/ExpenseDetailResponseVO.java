package com.secondproject.coupleaccount.vo.response;

import java.util.List;

import com.secondproject.coupleaccount.vo.ExpenseDetailVO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseDetailResponseVO {
    private Boolean status;
    private String message;
    private ExpenseDetailVO ExpenseDetail;
}
