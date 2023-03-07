package com.secondproject.coupleaccount.repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.secondproject.coupleaccount.entity.CategoryInfoEntity;
import com.secondproject.coupleaccount.entity.ExpenseInfoEntity;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.vo.ExpenseVO;
import com.secondproject.coupleaccount.vo.statistic.CateExpenseVO;
import com.secondproject.coupleaccount.vo.statistic.ExpenseCateVO;
import com.secondproject.coupleaccount.vo.MonthVO;
public interface ExpenseInfoRepository extends JpaRepository<ExpenseInfoEntity, Long> {
    public Integer countByEiSeq(Long eiSeq);
    //
    @Query(value = "select * from expense_info where ei_mbi_seq = :eiMbiSeq", nativeQuery = true)
    public List<ExpenseInfoEntity> findAllByEiMbiSeq(@Param("eiMbiSeq") Long eiMbiSeq);
    @Query(value = "select count(*) from expense_info where ei_seq = :eiSeq and ei_mbi_seq = :eiMbiSeq" , nativeQuery = true)
    public Integer countByEiSeqAndEiMbiSeq(@Param("eiSeq") Long eiSeq, @Param("eiMbiSeq") Long eiMbiSeq);
    //jpql
    // @Query("select sum(e.eiPrice) as totalExpense, e.eiDate as date from ExpenseInfoEntity e where e.memberBasicInfoEntity in (:seqs) and e.eiDate between :start and :end group by eiDate")
    // public List<ExpenseVO> findByExpense(@Param("seqs") List<MemberBasicInfoEntity> member,
    //         @Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query("select eiSeq as expenseSeq, IFNULL(eiPrice, 0) as expense, ei.categoryInfoEntity.cateName as category from ExpenseInfoEntity ei where ei.memberBasicInfoEntity in (:seqs) and ei.eiDate =:date")
    public List<ExpenseVO> findByExpense(@Param("seqs") List<MemberBasicInfoEntity> member,
            @Param("date") LocalDate date);
    @Query("select eiSeq as expenseSeq, IFNULL(eiPrice, 0) as expense, ei.categoryInfoEntity.cateName as category from ExpenseInfoEntity ei where ei.memberBasicInfoEntity.mbiSeq =:mid and ei.eiDate =:date and ei.eiStatus = 1")
    public List<ExpenseVO> findByExpenseByPerson(@Param("mid") Long mid ,@Param("date") LocalDate date);
    // 개인이 쓴 일일 지출 수입 금액
    @Query("select sum(e.eiPrice) as totalCateExpense, e.eiDate as date from ExpenseInfoEntity e where e.categoryInfoEntity in (:cate) and e.eiDate between :start and :end group by eiDate")
    public List<CateExpenseVO>  findByCategory (@Param("cate") List<CategoryInfoEntity> cate , @Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query(value =
        "select a.member_id as miId, IFNULL(a.expense_total, 0) as expenseSum, IFNULL(b.import_total, 0) as importSum, a.dt as dt from "+
        "(select e.ei_mbi_seq as member_id,sum(e.ei_price) as expense_total, e.ei_date as dt from expense_info e where e.ei_status =1 and e.ei_mbi_seq =:mid  and e.ei_date between :start and :end group by e.ei_date, e.ei_mbi_seq) a "+
        "left outer join"+
        "(select i.ii_mbi_seq as member_id, sum(i.ii_price) as import_total, i.ii_date as dt from import_info i where i.ii_status =1 and i.ii_mbi_seq =:mid  and i.ii_date between :start and :end group by i.ii_date, i.ii_mbi_seq) b "+
        "on a.dt = b.dt and a.member_id = b.member_id "+
        "union "+
        "select b.member_id, IFNULL(a.expense_total, 0), IFNULL(b.import_total, 0), b.dt from "+
        "(select e.ei_mbi_seq as member_id,sum(e.ei_price) as expense_total,e.ei_date as dt from expense_info e where e.ei_status =1 and e.ei_mbi_seq =:mid and e.ei_date between :start and :end group by e.ei_date, e.ei_mbi_seq) a "+
        "right outer join "+
        "(select i.ii_mbi_seq as member_id, sum(i.ii_price) as import_total, i.ii_date as dt from import_info i where i.ii_status =1 and i.ii_mbi_seq =:mid and i.ii_date between :start and :end group by i.ii_date, i.ii_mbi_seq) b "+
        "on a.dt = b.dt and a.member_id = b.member_id", nativeQuery=true
    )
    public List<MonthVO> getExpenseImportTotalList(@Param("mid") Long mid, @Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query("select sum(eiPrice) as extotal, ei.categoryInfoEntity.cateName as cate, ei.memberBasicInfoEntity.mbiSeq as member from ExpenseInfoEntity ei where ei.memberBasicInfoEntity in (:member) group by ei.categoryInfoEntity.cateName")
    public List<ExpenseCateVO> findByCateExpense(@Param("member") List<MemberBasicInfoEntity> member);
}