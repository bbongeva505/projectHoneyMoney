package com.secondproject.coupleaccount.vo.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
public interface ExpenseImportTotalVO {
    //  Integer getMid();
    @Schema(description = "지출 액" ,example = "20000")
     Integer getExtotal();
     Integer getImtotal();
    @Schema(description = "날짜" ,example = "2023-02-12")
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate getDt();
}
