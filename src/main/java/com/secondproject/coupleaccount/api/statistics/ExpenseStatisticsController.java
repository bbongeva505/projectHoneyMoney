package com.secondproject.coupleaccount.api.statistics;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.service.AccountBookInfoListService;
import com.secondproject.coupleaccount.service.ExpenseService;
import com.secondproject.coupleaccount.vo.response.CoupleMonthExpenseResponseVO;
import com.secondproject.coupleaccount.vo.statistic.ExpenseListVO;
import com.secondproject.coupleaccount.vo.statistic.ExpenseResponseVO;
import com.secondproject.coupleaccount.vo.statistic.StatisticVO;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class ExpenseStatisticsController {
  private final ExpenseService expenseService;
  private final AccountBookInfoListService accountBookInfoListService;
  private final MemberBasicInfoRepository mRepo;
  //?
  @GetMapping("list")
  public ResponseEntity<StatisticVO> getExpenseList(
  @RequestParam @Nullable String cate, 
  @RequestParam @Nullable Integer year,
  @RequestParam @Nullable Integer month,
  @RequestParam @Nullable Long bookNo   
  ){
    return new ResponseEntity<StatisticVO>(expenseService.getExpenseList(bookNo, year, month, cate),HttpStatus.OK);
  }
 //?
  @GetMapping("/total")
  public ResponseEntity<ExpenseListVO> getTotalList(
    @RequestParam Long bookNo
  ) {
    return new ResponseEntity<ExpenseListVO>(expenseService.TotalList(bookNo), HttpStatus.OK);
  }

  @Operation(summary = "월 별 커플통장 지출 내역")
  @GetMapping("/monthExpense/couple")
  public ResponseEntity<CoupleMonthExpenseResponseVO> getMonthExpenseByCouple(Authentication authentication,
      @Parameter(description = "검색어", example = "년도") @RequestParam Integer year, 
      @Parameter(description = "검색어", example = "월") @RequestParam Integer month) {
        Long mbiSeq = Long.parseLong(authentication.getName());
        MemberBasicInfoEntity entity = mRepo.findById(mbiSeq).get();      
    return new ResponseEntity<CoupleMonthExpenseResponseVO>(
        accountBookInfoListService.getMonthExpense(entity.getShareAccount().getSaiSeq(), year, month), HttpStatus.ACCEPTED);
  }
  //카테고리별 총 비용
  @Operation(summary = "카테고리 별 커플통장 총 합계")
  @GetMapping("/totalexpense")
  public ResponseEntity<ExpenseResponseVO>getCateTotal(
    Authentication authentication
  )
  {
     Long mbiSeq = Long.parseLong(authentication.getName());
        MemberBasicInfoEntity entity = mRepo.findById(mbiSeq).get(); 
    return new ResponseEntity<ExpenseResponseVO>(expenseService.cateExpense(entity.getShareAccount().getSaiSeq()), HttpStatus.OK);
  }
}




