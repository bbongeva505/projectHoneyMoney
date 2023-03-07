package com.secondproject.coupleaccount.repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.secondproject.coupleaccount.entity.ImportInfoEntity;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.vo.ImportVO;
public interface ImportInfoRepository extends JpaRepository<ImportInfoEntity, Long> {
    public Integer countByIiSeq(Long iiSeq);
    @Query(value = "select count(*) from import_info where ii_seq = :iiSeq and ii_mbi_seq = :iiMbiSeq", nativeQuery= true)
    public Integer countByIiSeqAndIiMbiSeq(@Param("iiSeq") Long iiSeq, @Param("iiMbiSeq") Long iiMbiSeq);
    @Query(value = "select * from import_info where ii_mbi_seq =:iiMbiSeq", nativeQuery = true)
    public List<ImportInfoEntity> findByIiMbiSeq(@Param("iiMbiSeq") Long iiMbiSeq);
    // @Query("select sum(i.iiPrice) as totalImport, i.iiDate as date from ImportInfoEntity i where i.memberBasicInfoEntity in(:seqs) and i.iiDate between :start and :end group by iiDate")
    // public List<ImportVO> findByImport(@Param("seqs") List<MemberBasicInfoEntity> member,
    //         @Param("start") LocalDate start, @Param("end") LocalDate end);
    public List<ImportInfoEntity> findByIiDate(LocalDate iiDate);
     @Query("select i.iiSeq as importSeq, IFNULL(i.iiPrice, 0) as income from  ImportInfoEntity i where i.memberBasicInfoEntity in (:seqs) and i.iiDate =:date")
    public List<ImportVO> findByIncome(@Param("seqs") List<MemberBasicInfoEntity> member,
            @Param("date") LocalDate date);
  @Query("select i.iiSeq as importSeq, IFNULL(i.iiPrice,0) as income from  ImportInfoEntity i where i.memberBasicInfoEntity.mbiSeq =:mid and i.iiDate =:date and i.iiStatus = 1")
    public List<ImportVO> findByIncomeByPerson(@Param("mid") Long mid,
            @Param("date") LocalDate date);
}