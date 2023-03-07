package com.secondproject.coupleaccount.vo.statistic;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secondproject.coupleaccount.vo.MonthExpenseVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticVO {
  private String msg;
  private Integer code;
  private String cate;
  private List<CateExpenseVO> cateList;
  @JsonIgnore
  private List<MonthExpenseVO> ExpenseList; 
  private Long seq;
}
