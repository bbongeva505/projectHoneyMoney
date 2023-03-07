package com.secondproject.coupleaccount.vo;

import java.time.LocalDate;

import com.secondproject.coupleaccount.entity.ExpenseInfoEntity;
import com.secondproject.coupleaccount.entity.ImportInfoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthAccountBookListVO {
    private LocalDate date;
    private Integer totalImport;
    private Integer totalExpense;

    public MonthAccountBookListVO(ImportInfoEntity entity,ExpenseInfoEntity entity2){
        this.totalImport = entity.getIiPrice();
        this.totalExpense = entity2.getEiPrice();
    }
    
}
