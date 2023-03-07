package com.secondproject.coupleaccount.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.coupleaccount.entity.ExpenseImportViewEntity;
import com.secondproject.coupleaccount.vo.MonthVO;

public interface ExpenseImportViewRepository extends JpaRepository<ExpenseImportViewEntity, Long> {
    @Query("select IFNULL(sum(e.expenseSum), 0) as expenseSum, IFNULL(sum(e.importSum),0) as importSum, e.Dt as dt from ExpenseImportViewEntity e where e.memberId in(:seqs) and e.Dt between :start and :end group by e.Dt")
    public List<MonthVO> findByMonthTotalList(@Param("seqs") List<Long> memberId, @Param("start") LocalDate start, @Param("end") LocalDate end);
    // @Query("select IFNULL(sum(e.expenseSum), 0) as expenseSum, IFNULL(sum(e.importSum), 0) as importSum, e.Dt as dt from ExpenseImportViewEntity e where e.memberId =:member and e.Dt between :start and :end group by e.Dt")
    // @Query("select IF(sum(e.expenseSum) is null, 0, sum(e.expenseSum)) as expenseSum, IF(sum(e.importSum) is null, 0, sum(e.importSum)) as importSum, e.Dt as dt from ExpenseImportViewEntity e where e.memberId =:member and e.Dt between :start and :end group by e.Dt")
    // public List<MonthVO> findByMonthTotalListByPerson(@Param("member") Integer member , @Param("start") LocalDate start, @Param("end") LocalDate end);
}
