package com.secondproject.coupleaccount.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.coupleaccount.entity.AccountBookImgEntity;

public interface AccountBookImgRepository extends JpaRepository<AccountBookImgEntity,Long> {
    public List<AccountBookImgEntity> findByAiUri(String aiUri);
    public AccountBookImgEntity findByAiImgName(String aiUri);
}
