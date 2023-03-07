package com.secondproject.coupleaccount;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.secondproject.coupleaccount.entity.ExpenseInfoEntity;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;
import com.secondproject.coupleaccount.repository.ExpenseImportViewRepository;
import com.secondproject.coupleaccount.repository.ExpenseInfoRepository;
import com.secondproject.coupleaccount.repository.ImportInfoRepository;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.repository.ShareAccountInfoRepository;
import com.secondproject.coupleaccount.vo.ExpenseDetailVO;
import com.secondproject.coupleaccount.vo.MonthVO;

@SpringBootTest
class CoupleaccountApplicationTests {

	@Test
	void contextLoads() {
	}

	
	@Autowired MemberBasicInfoRepository mbiRepo;
	@Test
	void testMemberJoin() {
		// System.out.println(mbiRepo.findAll());
		LocalDate date = LocalDate.parse("2022-01-01");

		System.out.println(mbiRepo.findByMbiBirth(date));
	}

	// @Autowired ImportInfoRepository importInfoRepository;
	// @Test
	// void testImportInfo() {
	// 	System.out.println(importInfoRepository.findAll());
	// }

	@Autowired
	ShareAccountInfoRepository shareAccountInfoRepository;
	@Autowired
	MemberBasicInfoRepository mRepo;
	@Autowired
	ExpenseInfoRepository eRepo;

	@Autowired
	ExpenseImportViewRepository eiRepo;
	@Test
	void findBySaiCode() {
		System.out.println(shareAccountInfoRepository.findBySaiCode("Z9dfOiTWrz"));
	}

	@Test
	void checkAccount() {
		ShareAccountInfoEntity shareAccountInfoEntity = shareAccountInfoRepository.findBySaiCode("Z9dfOiTWrz");
		System.out.println(mbiRepo.findByShareAccount(shareAccountInfoEntity));
	}

	@Test
	void Test1() {
		ExpenseInfoEntity entity = eRepo.findById(29L).orElse(null);
		if (entity.getAccountBookImgEntity() == null) {
			ExpenseDetailVO data = ExpenseDetailVO.builder().category(entity.getCategoryInfoEntity().getCateName())
					.price(entity.getEiPrice()).memo(entity.getEiMemo())
					.ImageUri(null).build();
			System.out.println(data);
		} else {
			ExpenseDetailVO data = ExpenseDetailVO.builder().category(entity.getCategoryInfoEntity().getCateName())
					.price(entity.getEiPrice()).memo(entity.getEiMemo())
					.ImageUri(entity.getAccountBookImgEntity().getAiUri()).build();
			System.out.println(data);
		}

	}

	@Test
	void getMonthCoupleExpense() {
		Long saiSeq=1L;
		Integer year=2023;
		Integer month= 2;
         ShareAccountInfoEntity share = shareAccountInfoRepository.findById(saiSeq).orElse(null);
        List<MemberBasicInfoEntity> member = mRepo.findByShareAccount(share);
        List<Long> seqs = new ArrayList<>();
        for (MemberBasicInfoEntity m : member) {
            seqs.add(m.getMbiSeq());
        }
        LocalDate firstDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        // String firstDateString = firstDate.format(DateTimeFormatter.ISO_DATE);
        // LocalDate start = LocalDate.parse(firstDate.format(DateTimeFormatter.ISO_DATE));
		LocalDate lastDate = firstDate.with(TemporalAdjusters.lastDayOfMonth());
		List<MonthVO> monthList = eiRepo.findByMonthTotalList(seqs, firstDate, lastDate);
		Integer MonthExpenseTotal = 0;
		
		for (MonthVO data : monthList) {
			// if(data.getExpenseSum() != null) {
				MonthExpenseTotal += data.getExpenseSum() != null ? data.getExpenseSum() : 0;
			// }
		}
		System.out.println("합계지출 : "+MonthExpenseTotal);
    }

}

