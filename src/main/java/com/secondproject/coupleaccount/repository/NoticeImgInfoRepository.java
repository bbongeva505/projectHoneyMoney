package com.secondproject.coupleaccount.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.coupleaccount.entity.NoticeImgInfoEntity;
import com.secondproject.coupleaccount.entity.NoticeInfoEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeImgInfoRepository extends JpaRepository<NoticeImgInfoEntity, Long> {
// public List<NoticeImgInfoEntity> findByNiiUri(String niiUri);
NoticeImgInfoEntity findByNiiUri(String niiUri);
public void deleteByNiiSeq(Long niiSeq);


public Page<NoticeImgInfoEntity> findByNiiSeq(Long niiSeq,Pageable pageable);
public Page<NoticeImgInfoEntity> findByNiiMbiSeq(Long niiMbiSeq, Pageable pageable);
public NoticeImgInfoEntity findByNoticeInfo(NoticeInfoEntity noticeInfo);

}
