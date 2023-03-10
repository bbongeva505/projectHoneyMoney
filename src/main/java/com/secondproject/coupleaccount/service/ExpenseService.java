package com.secondproject.coupleaccount.service;

import org.hibernate.metamodel.model.domain.internal.MapMember;
import org.springframework.stereotype.Service;

import com.secondproject.coupleaccount.entity.CategoryInfoEntity;
import com.secondproject.coupleaccount.entity.ExpenseInfoEntity;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;
import com.secondproject.coupleaccount.repository.CategoryInfoRepository;
import com.secondproject.coupleaccount.repository.ExpenseInfoRepository;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.repository.ShareAccountInfoRepository;
import com.secondproject.coupleaccount.vo.response.AccountBookResponseVO;
import com.secondproject.coupleaccount.vo.statistic.ExpenseListVO;
import com.secondproject.coupleaccount.vo.statistic.ExpenseResponseVO;
import com.secondproject.coupleaccount.vo.statistic.StatisticVO;
import com.secondproject.coupleaccount.vo.statistic.TotalExpenseVO;


import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
  private final ExpenseInfoRepository expenseInfoRepository;
  private final CategoryInfoRepository categoryInfoRepository;
  private final ShareAccountInfoRepository shareAccountInfoRepository;
  private final MemberBasicInfoRepository memberBasicInfoRepository;

  
  public StatisticVO getExpenseList(Long memberNo, Integer year, Integer month, String keyword){
    // ShareAccountInfoEntity share = shareAccountInfoRepository.findById(bookNo).orElse(null);
    MemberBasicInfoEntity member = memberBasicInfoRepository.findByMbiSeq(memberNo);
    // MemberBasicInfoEntity member = memberBasicInfoRepository.findById(memberNo).orElse(null);
    if (member == null) {
    StatisticVO result = StatisticVO.builder()
    .code(404)
    .msg(memberNo + "?????? ???????????? ????????????")
    .build();
    return result;   
    }
    
    List<CategoryInfoEntity> cate  = categoryInfoRepository.findByCateNameContains(keyword);
    // List<MemberBasicInfoEntity> member = memberBasicInfoRepository.findByShareAccount(share);
    LocalDate firstDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
    LocalDate lastDate = firstDate.with(TemporalAdjusters.lastDayOfMonth());
    StatisticVO result = StatisticVO.builder()
    .code(200)
    .msg(memberNo + "?????? ??? ??????????????? ?????????????????????.")
    .cate(keyword)
    .cateList(expenseInfoRepository.findByCategory(cate, firstDate, lastDate))
    .seq(memberNo)
    .build();
    return result;
  }

  // ????????? ?????????(????????????????????? ??????.)
  public ExpenseListVO TotalList(Long saiSeq) {
    ExpenseListVO response = ExpenseListVO.builder()
    .code(200)
    .msg(saiSeq + "?????? ????????? ??? ????????????????????? ?????????????????????.")
    .totalExpenseVO(totalList1(saiSeq))
    .build();
    return response;
}

public TotalExpenseVO totalList1(Long saiSeq) {
  TotalExpenseVO vo = new TotalExpenseVO();
    Integer totalExpense = 0;
    ShareAccountInfoEntity share = shareAccountInfoRepository.findById(saiSeq).orElse(null);
    if (share == null) {
        return null;
    }
    for (MemberBasicInfoEntity data : memberBasicInfoRepository.findByShareAccount(share)) {
        // list.add(data.getMbiSeq());
    
        for (ExpenseInfoEntity data1 : expenseInfoRepository.findAllByEiMbiSeq(data.getMbiSeq())) {
            totalExpense += data1.getEiPrice();
        }
    }
    vo.setTotalExpense(totalExpense);
    return vo;
}
// ??????????????? ?????? (????????????)
public ExpenseResponseVO cateExpense(Long saiSeq){
  ShareAccountInfoEntity share = shareAccountInfoRepository.findById(saiSeq).orElse(null);
    if (share == null) {
    ExpenseResponseVO result = ExpenseResponseVO.builder()
    .code(404)
    .msg( "???????????? ????????????")
    .build();
    return result;   
    }
    List<MemberBasicInfoEntity> member = memberBasicInfoRepository.findByShareAccount(share);
    List<Long> member1 = new ArrayList<>();
    for (MemberBasicInfoEntity m : member) {
        member1.add(m.getMbiSeq());
    }
    ExpenseResponseVO result = ExpenseResponseVO.builder()
    .List(expenseInfoRepository.findByCateExpense(member))
    .code(200)
    .msg("?????? ????????? ?????????????????????.")
    .build();
    return result;

}
}
