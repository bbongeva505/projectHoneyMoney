package com.secondproject.coupleaccount.vo.response;

import java.util.List;

import com.secondproject.coupleaccount.vo.MonthVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountImportByPersonResponseVO {
    private boolean status;
    private String message;
    private List<MonthVO> ExpenseImportTotalList;
}
