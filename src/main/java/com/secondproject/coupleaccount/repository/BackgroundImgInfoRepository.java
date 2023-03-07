package com.secondproject.coupleaccount.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.coupleaccount.entity.BackgroundImgInfoEntity;



public interface BackgroundImgInfoRepository extends JpaRepository<BackgroundImgInfoEntity, Long> {
    public List<BackgroundImgInfoEntity> findByBiiUri(String biiUri);
}
