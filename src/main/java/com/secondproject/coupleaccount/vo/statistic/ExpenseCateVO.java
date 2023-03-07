package com.secondproject.coupleaccount.vo.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

public interface ExpenseCateVO {
  @Schema(description = "카테고리 총 금액",example="100") Integer getExtotal();
  @JsonIgnore
  Long getMember();
  @Schema(description = "카테고리 명",example="카페")
  String  getCate();
  
}