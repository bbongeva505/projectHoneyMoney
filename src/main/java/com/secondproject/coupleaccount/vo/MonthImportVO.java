package com.secondproject.coupleaccount.vo;

import java.time.LocalDate;

// import com.secondproject.coupleaccount.entity.ImportInfoEntity;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Builder
public interface MonthImportVO {
    LocalDate getDate();
    String getTotalImport();
    
    
}
    