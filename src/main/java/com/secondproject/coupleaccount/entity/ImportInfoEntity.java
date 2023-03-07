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
@Table(name="import_Info")
public class ImportInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ii_seq") private Long iiSeq;
    @Column(name="ii_price") private Integer iiPrice;
    @Column(name="ii_memo") private String iiMemo;
    @Column(name="ii_date") private LocalDate iiDate;
    @Column(name = "ii_status") private Integer iiStatus;
    @ManyToOne @JoinColumn(name="ii_mbi_seq") private MemberBasicInfoEntity memberBasicInfoEntity;
}
