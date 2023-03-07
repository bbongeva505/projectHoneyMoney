package com.secondproject.coupleaccount.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.coupleaccount.entity.ExpenseImportByPersonViewEntity;
import com.secondproject.coupleaccount.vo.ExpenseImportByPersonViewVO;

// public interface ExpenseImportByPersonViewRepository extends JpaRepository<ExpenseImportByPersonViewEntity, Long> {
//      // @Query("select e.expenseSum as expenseSum e.importSum as importSum,e.dt as Dt from ExpenseImportByPersonViewEntity e where e.memberId =:no and e.dt between :start and :end")
//      // public List<ExpenseImportByPersonViewVO> findByMonthTotalListByPerson(@Param("no") Integer no, @Param("start") LocalDate start, @Param("end") LocalDate end);
//        @Query("select * from ExpenseImportByPersonViewEntity e where e.memberId =:no and e.dt between :start and :end")
//      public List<ExpenseImportByPersonViewVO> findByMonthTotalListByPerson(@Param("no") Long no, @Param("start") LocalDate start, @Param("end") LocalDate end);
// }
