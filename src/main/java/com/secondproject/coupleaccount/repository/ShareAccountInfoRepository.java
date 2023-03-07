package com.secondproject.coupleaccount.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;

public interface ShareAccountInfoRepository extends JpaRepository<ShareAccountInfoEntity, Long> {
    ShareAccountInfoEntity findBySaiCode(String saiCode);
}
