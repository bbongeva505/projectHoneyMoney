package com.secondproject.coupleaccount.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.NoticeInfoEntity;

public interface NoticeInfoRepository extends JpaRepository<NoticeInfoEntity,Long> {
  public List<NoticeInfoEntity> findAllByNiSeq(Long niSeq);
  // public List<NoticeInfoEntity> findByNiSeq(Long niSeq);
  public void deleteByNiSeq(Long niSeq);
  // public Long conutByNiSeq(Long niSeq);
  public Page<NoticeInfoEntity> findByNiMemoContains(String niMemo, Pageable pageable);
  // public Page<NoticeInfoEntity> findByNiMbiSeq(Long niMbiSeq, Pageable pageable);
  public List<NoticeInfoEntity> findByMemberInfo (MemberBasicInfoEntity memberInfo);
  // public NoticeInfoEntity findByNiNiiSeq(Long niNiiSeq); 
  public NoticeInfoEntity findByNiSeq(Long niSeq);

}
