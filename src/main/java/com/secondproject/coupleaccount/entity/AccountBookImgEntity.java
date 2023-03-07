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
@Table(name="account_book_img")
public class AccountBookImgEntity {
     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ai_seq") private Long aiSeq;
    @Column(name="ai_img_name") private String aiImgName;
    @Column(name="ai_uri") private String aiUri;
}
