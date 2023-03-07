package com.secondproject.coupleaccount.vo;
import java.time.LocalDate;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Builder
public interface MonthExpenseVO {
    LocalDate getDate();
    Integer getTotalExpense();
}


