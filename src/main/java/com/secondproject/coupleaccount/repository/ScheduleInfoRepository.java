package com.secondproject.coupleaccount.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ScheduleInfoEntity;
import com.secondproject.coupleaccount.vo.schdule.schduleListVO;
import com.secondproject.coupleaccount.vo.schdule.schduleListVO1;

public interface ScheduleInfoRepository extends JpaRepository<ScheduleInfoEntity, Long> {
    ScheduleInfoEntity findBySiSeq(Long siSeq);
    public Integer countBySiSeq(Long siSeq);
    Page<ScheduleInfoEntity> findAll(Pageable pageable);

    @Query(value = "select * from schedule_info where si_mi_seq = :siMiSeq", nativeQuery = true)
    public List<ScheduleInfoEntity> findAllBySiMiSeq(@Param("siMiSeq") Long siMiSeq);

    @Query(value = "select count(*) from schedule_info where si_seq = :siSeq and si_mi_seq = :siMiSeq" , nativeQuery = true)
    public Integer countBySiSeqAndSiMiSeq(@Param("siSeq") Long siSeq, @Param("siMiSeq") Long siMiSeq);


    // select si_memo ,si_start_date, si_end_date, si_mi_seq from schedule_info where si_mi_seq BETWEEN 1 and 2;
    @Query("select si.siStartDate as stdate, si.scheduleimg.siiUri as scheimg ,si.memberbasicinfo.mbiSeq as mbiseq, si.siMemo as memo from ScheduleInfoEntity si where si.memberbasicinfo in (:seqs) and si.siStartDate between :start and :end order by stdate asc")
    public List<schduleListVO> findByCoupleScheduleInfo(@Param("seqs") List<MemberBasicInfoEntity> member, @Param("start") LocalDate start, @Param("end") LocalDate end);


    @Query("select si.siSeq as siseq, si.siStartDate as stdate,si.memberbasicinfo.mbiSeq as mbiseq, si.siMemo as memo from ScheduleInfoEntity si where si.memberbasicinfo in (:seqs) and si.siStartDate between :start and :end order by stdate asc")
    public List<schduleListVO1> findByCoupleScheduleInfo1(@Param("seqs") List<MemberBasicInfoEntity> member, @Param("start") LocalDate start, @Param("end") LocalDate end);

}
// @Query("select eiSeq as seq, eiPrice as expense, ei.categoryInfoEntity.cateName as category from ExpenseInfoEntity ei where ei.memberBasicInfoEntity in (:seqs) and ei.eiDate =:date")
//     public List<ExpenseVO> findByExpense(@Param("seqs") List<MemberBasicInfoEntity> member,
//             @Param("date") LocalDate date);


// select eiSeq as seq, eiPrice as expense, ei.categoryInfoEntity.cateName as category from ExpenseInfoEntity ei where ei.memberBasicInfoEntity.mbiSeq =:mid and ei.eiDate =:date and ei.eiStatus = 1


