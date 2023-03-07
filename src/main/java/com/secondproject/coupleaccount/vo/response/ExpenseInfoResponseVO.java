package com.secondproject.coupleaccount.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseInfoResponseVO {
    private boolean status;
    private String message;
}
