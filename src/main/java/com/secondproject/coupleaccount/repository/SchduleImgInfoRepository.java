package com.secondproject.coupleaccount.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.coupleaccount.entity.ScheduleImgInfoEntity;

public interface SchduleImgInfoRepository extends JpaRepository<ScheduleImgInfoEntity, Long> {
    public List<ScheduleImgInfoEntity> findBySiiUri(String siiUri);
}