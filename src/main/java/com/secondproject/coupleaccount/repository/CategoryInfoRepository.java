package com.secondproject.coupleaccount.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.coupleaccount.entity.CategoryInfoEntity;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfoEntity, Long> {
    //jpa
    public Integer countByCateSeq(Long cateSeq);
    List<CategoryInfoEntity> findByCateNameContains(String cateName);
}
