package com.secondproject.coupleaccount.vo.response;

import java.util.List;
import com.secondproject.coupleaccount.vo.MonthVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBookListResponseVO {
    private Boolean status;
    private String message;
    private List<MonthVO> month; 
    // private List<MonthImportVO> importList;
}

