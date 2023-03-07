package com.secondproject.coupleaccount.entity;
import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name="member_basic_info")
public class MemberBasicInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="mbi_seq") private Long mbiSeq;
    @Column(name="mbi_password") private String mbiPassword;
    @Column(name="mbi_gender") private Integer mbiGender;
    @Column(name="mbi_name") private String mbiName;
    @Column(name="mbi_start_day") private LocalDate mbiStartDay;
    @Column(name="mbi_birth") private LocalDate mbiBirth;
    @Column(name="mbi_nickname") private String mbiNickName;
    // @Column(name="mbi_sai_seq") private Long mbiSaiSeq;
    @ManyToOne @JoinColumn(name = "mbi_sai_seq") ShareAccountInfoEntity shareAccount;
    @OneToOne @JoinColumn(name="mbi_bii_seq") BackgroundImgInfoEntity backgroundImgInfoEntity;
    @Column(name="mbi_basic_email") private String mbiBasicEmail;    
}
