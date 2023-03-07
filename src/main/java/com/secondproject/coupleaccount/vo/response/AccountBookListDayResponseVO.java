package com.secondproject.coupleaccount.vo.response;

import java.util.List;

import com.secondproject.coupleaccount.vo.ExpenseVO;
import com.secondproject.coupleaccount.vo.ImportVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBookListDayResponseVO {
    private boolean status;
    private String message;
    private List<ExpenseVO> ExpenseList;
    private List<ImportVO> imcomeList;
}
