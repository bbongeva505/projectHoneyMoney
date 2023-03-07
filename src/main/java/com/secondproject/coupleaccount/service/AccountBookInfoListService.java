package com.secondproject.coupleaccount.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.secondproject.coupleaccount.entity.ExpenseInfoEntity;
import com.secondproject.coupleaccount.entity.ImportInfoEntity;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;
import com.secondproject.coupleaccount.repository.ExpenseImportViewRepository;
import com.secondproject.coupleaccount.repository.ExpenseInfoRepository;
import com.secondproject.coupleaccount.repository.ImportInfoRepository;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.repository.ShareAccountInfoRepository;
import com.secondproject.coupleaccount.vo.ExpenseDetailVO;
import com.secondproject.coupleaccount.vo.ExpenseVO;
import com.secondproject.coupleaccount.vo.ImportDetailVO;
import com.secondproject.coupleaccount.vo.MonthVO;
import com.secondproject.coupleaccount.vo.MyAccountInfoVO;
import com.secondproject.coupleaccount.vo.response.AccountBookListDayResponseVO;
import com.secondproject.coupleaccount.vo.response.AccountBookListResponseVO;
import com.secondproject.coupleaccount.vo.response.AccountBookResponseVO;
import com.secondproject.coupleaccount.vo.response.AccountImportByPersonResponseVO;
import com.secondproject.coupleaccount.vo.response.CoupleMonthExpenseResponseVO;
import com.secondproject.coupleaccount.vo.response.ExpenseDetailResponseVO;
import com.secondproject.coupleaccount.vo.response.ImportDetailResponseVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountBookInfoListService {
    private final MemberBasicInfoRepository mRepo;
    private final ExpenseInfoRepository eRepo;
    private final ImportInfoRepository iRepo;
    private final ShareAccountInfoRepository sRepo;
    private final ExpenseImportViewRepository viewRepo;
    // private final Account

    // 총지출수입 리스트(공유통장번호에 따른.)
    public AccountBookResponseVO TotalList(Long saiSeq) {
        AccountBookResponseVO result = AccountBookResponseVO.builder().status(true)
                .message(saiSeq + "번의 가계부 총 수입지출정보가 조회되었습니다.")
                .myAccountInfoVO(totalList1(saiSeq)).build();
        return result;
    }

    public MyAccountInfoVO totalList1(Long saiSeq) {
        MyAccountInfoVO vo = new MyAccountInfoVO();
        Integer totalImport = 0;
        Integer totalExpense = 0;
        ShareAccountInfoEntity share = sRepo.findById(saiSeq).orElse(null);
        if (share == null) {
            return null;
        }
        for (MemberBasicInfoEntity data : mRepo.findByShareAccount(share)) {
            // list.add(data.getMbiSeq());
            for (ImportInfoEntity data1 : iRepo.findByIiMbiSeq(data.getMbiSeq())) {
                totalImport += data1.getIiPrice();
            }
            for (ExpenseInfoEntity data2 : eRepo.findAllByEiMbiSeq(data.getMbiSeq())) {
                totalExpense += data2.getEiPrice();
            }
        }
        vo.setTotalImport(totalImport);
        vo.setTotalExpense(totalExpense);
        vo.setBalance(totalImport - totalExpense);
        return vo;
    }
    // 공용 통장 월 총 지출 수입 조회
    public AccountBookListResponseVO getMonthAccountBookList(Long saiSeq, Integer year, Integer month) {
        ShareAccountInfoEntity share = sRepo.findById(saiSeq).orElse(null);
        if (share == null) {
            AccountBookListResponseVO result = AccountBookListResponseVO.builder().status(true)
                    .message(saiSeq + "번은 존재하지 않습니다").build();
            return result;
        }
        List<MemberBasicInfoEntity> member = mRepo.findByShareAccount(share);
        List<Long> seqs = new ArrayList<>();
        for (MemberBasicInfoEntity m : member) {
            seqs.add(m.getMbiSeq());
        }
        // List<ExpenseImportViewEntity> memberId = member;
        // for (MemberBasicInfoEntity data :  {
        //     member.add(data);
        // }
        // LocalDate date = LocalDate.now();
        LocalDate firstDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        // String firstDateString = firstDate.format(DateTimeFormatter.ISO_DATE);
        // LocalDate start = LocalDate.parse(firstDate.format(DateTimeFormatter.ISO_DATE));
        LocalDate lastDate = firstDate.with(TemporalAdjusters.lastDayOfMonth());
        // LocalDate end = LocalDate.parse(lastDate.format(DateTimeFormatter.ISO_DATE));
        if (viewRepo.findByMonthTotalList(seqs, firstDate, lastDate) == null) {
            AccountBookListResponseVO result = AccountBookListResponseVO.builder().status(true)
                    .message(saiSeq + "조회된 것이 없습니다.")
                    .month(viewRepo.findByMonthTotalList(null, firstDate, lastDate)).build();
            return result;
        }
        AccountBookListResponseVO result = AccountBookListResponseVO.builder().status(true)
                .message(saiSeq + "번의 " + month + "월 지출 및 수입정보가 조회되었습니다.")
                // .ExpenseList(eRepo.findByExpense(member, firstDate, lastDate))
                .month(viewRepo.findByMonthTotalList(seqs, firstDate, lastDate)).build();
        return result;
    }

    // public AccountBookListResponseVO getMonthBy

    // 공용통장 일 총 수입 지출 조회
    public AccountBookListDayResponseVO getDayAccountBookList(Long saiSeq, Integer year, Integer month, Integer day) {
        ShareAccountInfoEntity share = sRepo.findById(saiSeq).orElse(null);
        if (share == null) {
            AccountBookListDayResponseVO result = AccountBookListDayResponseVO.builder().status(true)
                    .message(saiSeq + "번은 존재하지 않습니다").build();
            return result;
        }
        List<MemberBasicInfoEntity> member = mRepo.findByShareAccount(share);
        List<Long> seqs = new ArrayList<>();
        for (MemberBasicInfoEntity m : member) {
            seqs.add(m.getMbiSeq());
        }
        LocalDate date = LocalDate.of(year, month, day);
        AccountBookListDayResponseVO result = AccountBookListDayResponseVO.builder().status(true)
                .message(date + "일의 수입 및 지출내역입니다.")
                .ExpenseList(eRepo.findByExpense(member, date)).imcomeList(iRepo.findByIncome(member, date)).build();
        return result;
    }
    
    
    public ExpenseDetailResponseVO getExpenseDetail(Long eiSeq) {
        ExpenseInfoEntity entity = eRepo.findById(eiSeq).orElse(null);
        if (entity == null) {
            ExpenseDetailResponseVO result = ExpenseDetailResponseVO.builder().status(false)
                    .message(eiSeq + "번은 알맞지 않은 번호입니다.").ExpenseDetail(null).build();
            return result;
        } else if (entity.getAccountBookImgEntity() == null) {
            ExpenseDetailVO data = ExpenseDetailVO.builder().category(entity.getCategoryInfoEntity().getCateName())
                    .price(entity.getEiPrice()).memo(entity.getEiMemo())
                    .ImageUri(null).expenseStatus(entity.getEiStatus()).build();
            ExpenseDetailResponseVO result = ExpenseDetailResponseVO.builder().status(true)
                    .message(eiSeq + "번의 지출 상세정보가 조회되었습니다.").ExpenseDetail(data).build();

            return result;

        } else {
            System.out.println("aaaaaaaaaaa"+entity.getAccountBookImgEntity().getAiImgName());
            ExpenseDetailVO data = ExpenseDetailVO.builder().category(entity.getCategoryInfoEntity().getCateName())
                    .price(entity.getEiPrice()).memo(entity.getEiMemo())
                    .ImageUri(entity.getAccountBookImgEntity().getAiImgName()).expenseStatus(entity.getEiStatus()).build();
            ExpenseDetailResponseVO result = ExpenseDetailResponseVO.builder().status(true)
                    .message(eiSeq + "번의 지출 상세정보가 조회되었습니다.").ExpenseDetail(data).build();

            return result;
        }

    }

    public ImportDetailResponseVO getImportDetail(Long iiSeq) {
        ImportInfoEntity entity = iRepo.findById(iiSeq).orElse(null);
        if (entity == null) {
            ImportDetailResponseVO result = ImportDetailResponseVO.builder().status(false)
                    .message(iiSeq + "번은 알맞지 않은 번호입니다.")
                    .importDetail(null).build();
            return result;
        }

        ImportDetailVO data = ImportDetailVO.builder().price(entity.getIiPrice()).memo(entity.getIiMemo())
                .importStatus(entity.getIiStatus()).build();
        ImportDetailResponseVO result = ImportDetailResponseVO.builder().status(true)
                .message(iiSeq + "번의 입금 상세정보가 조회되었습니다.").importDetail(data).build();
        return result;
    }
    // 본인 월 총 수입 지출 조회 
    public AccountImportByPersonResponseVO getExpenseImportByPersonTotalList(Long miSeq, Integer year, Integer month) {
        LocalDate firstDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDate = firstDate.with(TemporalAdjusters.lastDayOfMonth());
        MemberBasicInfoEntity entity = mRepo.findById(miSeq).orElse(null);
        if (entity == null) {
            AccountImportByPersonResponseVO result = AccountImportByPersonResponseVO.builder().status(false)
                    .message(miSeq + "번은 알맞지 않은 번호입니다.").ExpenseImportTotalList(null).build();
            return result;
        }
        // else if (eRepo.getExpenseImportTotalList(status, mbiSeq, firstDate, lastDate) == null) {
        //     AccountImportByPersonResponseVO result = AccountImportByPersonResponseVO.builder().status(false)
        //             .message(mbiSeq + "번은 조회된 것이 없습니다.").ExpenseImportTotalList(null).build();
        //     return result;
        // }
        AccountImportByPersonResponseVO result = AccountImportByPersonResponseVO.builder().status(true)
                .message(miSeq + "번의 " + month + "월 수입지출이 조회되었습니다.")
                .ExpenseImportTotalList(eRepo.getExpenseImportTotalList(miSeq, firstDate, lastDate)).build();
        return result;
    }
    // 본인 일일 총 지출 수입 조회 
    public AccountBookListDayResponseVO getDayAccountBookByPersonList(Long miSeq, Integer year, Integer month,
            Integer day) {
        MemberBasicInfoEntity entity = mRepo.findById(miSeq).orElse(null);
        if (entity == null) {
            AccountBookListDayResponseVO result = AccountBookListDayResponseVO.builder().status(true)
                    .message(miSeq + "번은 존재하지 않습니다").build();
            return result;
        }
        LocalDate date = LocalDate.of(year, month, day);
        AccountBookListDayResponseVO result = AccountBookListDayResponseVO.builder().status(true)
                .message(date + "일의 수입 및 지출내역입니다.")
                .ExpenseList(eRepo.findByExpenseByPerson(miSeq, date))
                .imcomeList(iRepo.findByIncomeByPerson(miSeq, date)).build();
        return result;
    }

    //  통계쪽
     public CoupleMonthExpenseResponseVO getMonthExpense(Long saiSeq, Integer year, Integer month) {
         ShareAccountInfoEntity share = sRepo.findById(saiSeq).orElse(null);
        List<MemberBasicInfoEntity> member = mRepo.findByShareAccount(share);
        List<Long> seqs = new ArrayList<>();
        for (MemberBasicInfoEntity m : member) {
            seqs.add(m.getMbiSeq());
        }
        LocalDate firstDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        // String firstDateString = firstDate.format(DateTimeFormatter.ISO_DATE);
        // LocalDate start = LocalDate.parse(firstDate.format(DateTimeFormatter.ISO_DATE));
		LocalDate lastDate = firstDate.with(TemporalAdjusters.lastDayOfMonth());
        List<MonthVO> monthList = viewRepo.findByMonthTotalList(seqs, firstDate, lastDate);
		Integer MonthExpenseTotal = 0;
        for (MonthVO data : monthList) {
            MonthExpenseTotal += data.getExpenseSum() != null ? data.getExpenseSum() : 0;
        }
        CoupleMonthExpenseResponseVO result = CoupleMonthExpenseResponseVO.builder().status(true)
                .message("커플 " + saiSeq + "번의 " +month+"월 지출합계가 조회되었습니다.").ExpenseTotal(MonthExpenseTotal).build();
        return result;
     }
    //  상대방 월 총 지출수입 조회 
     public AccountImportByPersonResponseVO getExpenseImportByOtherPersonTotalList(Long miSeq, Integer year,
            Integer month) {
        LocalDate firstDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDate = firstDate.with(TemporalAdjusters.lastDayOfMonth());
        MemberBasicInfoEntity entity = mRepo.findById(miSeq).orElse(null);
        if (entity == null) {
            AccountImportByPersonResponseVO result = AccountImportByPersonResponseVO.builder().status(false)
                    .message(miSeq + "번은 알맞지 않은 번호입니다.").ExpenseImportTotalList(null).build();
            return result;
        }
        List<MemberBasicInfoEntity> member = mRepo.findByShareAccount(entity.getShareAccount());
        Long seq = null;
        for (MemberBasicInfoEntity m : member) {
            if (!m.getMbiBasicEmail().equals(entity.getMbiBasicEmail())) {
                seq = m.getMbiSeq();
            }
        }
        AccountImportByPersonResponseVO result = AccountImportByPersonResponseVO.builder().status(true)
                    .message(miSeq + "번의 상대방 " + month + "월 수입지출이 조회되었습니다.")
                    .ExpenseImportTotalList(eRepo.getExpenseImportTotalList(seq, firstDate, lastDate)).build();
        return result;
    }
    // 상대방 일일 총 지출수입 조회
    public AccountBookListDayResponseVO getDayAccountBookByOtherPersonList(Long miSeq, Integer year, Integer month,
            Integer day) {
        MemberBasicInfoEntity entity = mRepo.findById(miSeq).orElse(null);
        if (entity == null) {
            AccountBookListDayResponseVO result = AccountBookListDayResponseVO.builder().status(true)
                    .message(miSeq + "번은 존재하지 않습니다").build();
            return result;
        }
         List<MemberBasicInfoEntity> member = mRepo.findByShareAccount(entity.getShareAccount());
        Long seq = null;
        for (MemberBasicInfoEntity m : member) {
            if (!m.getMbiBasicEmail().equals(entity.getMbiBasicEmail())) {
                seq = m.getMbiSeq();
            }
        }
        LocalDate date = LocalDate.of(year, month, day);
        AccountBookListDayResponseVO result = AccountBookListDayResponseVO.builder().status(true)
                .message(date + "일의 상대방 수입 및 지출내역입니다.")
                .ExpenseList(eRepo.findByExpenseByPerson(seq, date))
                .imcomeList(iRepo.findByIncomeByPerson(seq, date)).build();
        return result;
    }
        
    }


    // int month = 6;
    // LocalDate firstDate = LocalDate.of(0, month, 0).with(TemporalAdjusters.firstDayOfMonth());
    // String firstDateString = firstDate.format(DateTimeFormatter.ISO_DATE);
    // System.out.println(firstDateString);

    // 월,일별가게부 조회 (공통)
    // public AccountBookListResponseVO getShareAccountBookList(Long saiSeq, String type, Integer year, Integer month,
    //         Integer date) {
    //     if (sRepo.countBySaiSeq(saiSeq) == 0) {
    //         AccountBookListResponseVO result = AccountBookListResponseVO.builder().message("존재하지 않는 번호입니다.")
    //                 .status(false).monthList(null).build();
    //         return result;
    //     }

    // for (MemberBasicInfoEntity data : mRepo.findAllByMbiSaiSeq(saiSeq)) {
    //     if (type == "month") {
    //         //   for (ImportInfoEntity data1 : iRepo.findByIiMbiSeq(data.getMbiSeq())) {
    //         //         if(data1.getIiDate().getYear() == year && data1.getIiDate().getMonthValue() == month) {
    //         //             MonthImportVO vo1 = new MonthImportVO(data1);
    //         //             for (ExpenseInfoEntity data2 : eRepo.findAllByEiMbiSeq(data.getMbiSeq())) {
    //         //                 if(data2.getEiDate() == data1.getIiDate())
    //         //             }
    //         //         }
    //         //     }   

    //         for (ImportInfoEntity data1 : iRepo.findByIiMbiSeq(data.getMbiSeq())) {
    //             if (data1.getIiDate().getYear() == year && data1.getIiDate().getMonthValue() == month) {
    //                 MonthAccountBookListVO vo1 = new MonthAccountBookListVO();
    //                 vo1.setTotalImport(data1.getIiPrice());
    //             }
    //         }
    //     }
    // }
    // }

    // public MonthAccountBookListVO getMonthAccountBook(Long mbiSeq, Integer year, Integer month) {
    //     List<Object> importList = new LinkedList<>();
    //     for (ImportInfoEntity data1 : iRepo.findByIiMbiSeq(mbiSeq)) {
    //         if (data1.getIiDate().getYear() == year && data1.getIiDate().getMonthValue() == month) {

    //         }
    //     }
    // }

