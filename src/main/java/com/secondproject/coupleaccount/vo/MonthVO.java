package com.secondproject.coupleaccount.vo;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
public interface MonthVO {
    @Schema(description = "날짜" ,example = "2023-02-12")
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate getDt();
    @Schema(description = "지출 액" ,example = "20000")
    Integer getExpenseSum();
    @Schema(description = "입금 액" ,example = "670000")
    Integer getImportSum();
    // @Schema(description = "멤버" ,example = "670000")
    // Integer getMember();
}