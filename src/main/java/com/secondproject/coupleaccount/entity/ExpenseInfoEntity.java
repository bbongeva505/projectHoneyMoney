// package com.secondproject.coupleaccount.entity;
// import java.time.LocalDate;

// import org.hibernate.annotations.DynamicInsert;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Entity
// @Builder
// @DynamicInsert
// @Table(name="expense_Info")
// public class ExpenseInfoEntity {
//     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name="ei_seq") private Long eiSeq;
//     @Column(name="ei_price") private Integer eiPrice;
//     @Column(name="ei_memo") private String eiMemo;
//     @Column(name="ei_date") private LocalDate eiDate;
//     @Column(name="ei_status") private Integer eiStatus;
//     @Column(name="ii_mbi_seq") private Long iiMbiSeq;
//     @Column(name="ei_ai_seq") private Long eiAiSeq;
//     @Column(name="ei_cate_seq") private Long eiCateSeq;
// }

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
@Table(name="expense_Info")
public class ExpenseInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ei_seq") private Long eiSeq;
    @Column(name="ei_price") private Integer eiPrice;
    @Column(name="ei_memo") private String eiMemo;
    @Column(name="ei_date") private LocalDate eiDate;
    @Column(name = "ei_status")
    private Integer eiStatus;

    public ExpenseInfoEntity(Long eiSeq, Integer eiPrice, String eiMemo, LocalDate eiDate, Integer eiStatus) {
        this.eiSeq = eiSeq;
        this.eiPrice = eiPrice;
        this.eiMemo = eiMemo;
        this.eiDate = eiDate;
        this.eiStatus = eiStatus;
    }
    @ManyToOne @JoinColumn(name="ei_mbi_seq") private MemberBasicInfoEntity memberBasicInfoEntity;
    @OneToOne @JoinColumn(name="ei_ai_seq") private AccountBookImgEntity accountBookImgEntity;
    @OneToOne @JoinColumn(name="ei_cate_seq") private CategoryInfoEntity categoryInfoEntity;
}
