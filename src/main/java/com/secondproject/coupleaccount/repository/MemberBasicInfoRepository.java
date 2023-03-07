package com.secondproject.coupleaccount.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;

public interface MemberBasicInfoRepository extends JpaRepository<MemberBasicInfoEntity, Long> {
    public MemberBasicInfoEntity findByMbiNameAndMbiPassword(String name, String pwd);
    public List<MemberBasicInfoEntity> findByShareAccount(ShareAccountInfoEntity shareAccount);
    public MemberBasicInfoEntity findByMbiSeq(Long miSeq);
    public MemberBasicInfoEntity findByMbiBasicEmailAndMbiPassword(String email, String pwd);

    public Optional<MemberBasicInfoEntity> findByMbiBasicEmail(String email);

    public Integer countByMbiSeq(Long mbiSeq);

    public MemberBasicInfoEntity findByMbiNameAndMbiBirthAndMbiBasicEmail(String name, LocalDate birth, String Email);

    public MemberBasicInfoEntity findByMbiBirth(LocalDate birth);
}
