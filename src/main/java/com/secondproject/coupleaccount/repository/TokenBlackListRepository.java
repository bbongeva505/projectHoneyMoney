package com.secondproject.coupleaccount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.coupleaccount.entity.TokenBlackList;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, String>{
    
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM TokenBlackList t WHERE t.expireTime <= :now")
    void deleteAllbyExpireTimeBeforeNow(@Param("now") Long now);
}
