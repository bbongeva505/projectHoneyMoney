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
@Table(name="schedule_info")
public class ScheduleInfoEntity {
     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="si_seq") private Long siSeq;
    @Column(name="si_start_date") private LocalDate siStartDate;
    @Column(name="si_end_date") private LocalDate siEndDate;
    @Column(name="si_memo") private String siMemo;
    @ManyToOne @JoinColumn(name = "si_sii_seq") ScheduleImgInfoEntity scheduleimg;
    @ManyToOne @JoinColumn(name = "si_mi_seq") MemberBasicInfoEntity memberbasicinfo;    
}
