package com.secondproject.coupleaccount.vo;
import io.swagger.v3.oas.annotations.media.Schema;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Builder
public interface ImportVO {
     @Schema(description = "입금 고유번호 " ,example = "1")
     Long getImportSeq();
     @Schema(description = "입금액 " ,example = "670000")
     Integer getIncome();
}
    