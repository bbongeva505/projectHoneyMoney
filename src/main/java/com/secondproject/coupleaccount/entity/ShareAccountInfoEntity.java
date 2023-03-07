package com.secondproject.coupleaccount.entity;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="share_account_info")
public class ShareAccountInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sai_seq") private Long saiSeq;
    @Column(name="sai_name") private String saiName;
    @Column(name="sai_start_day") private LocalDate saiStartDay ;
    @Column(name="sai_code") private String saiCode;
}
