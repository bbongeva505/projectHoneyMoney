package com.secondproject.coupleaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImportDetailVO {
    @Schema(description = "입금액 " ,example = "600000")
    private Integer price;
     @Schema(description = "입금 메모 " ,example = "입금메모1")
     private String memo;
     @Schema(description = "상태" ,example = "0.본인 1.공동")
    private Integer importStatus;
}
