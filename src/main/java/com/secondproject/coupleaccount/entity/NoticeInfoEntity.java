package com.secondproject.coupleaccount.entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name="notice_info")
public class NoticeInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ni_seq") @Schema(description="공지사항 번호", example="1") private Long niSeq;
    @Column(name="ni_memo")@Schema(description="공지사항 내용", example="여행 - 통도사") private String niMemo ;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")   
    @Column(name="ni_date")@Schema(description="공지사항 등록일", example="2023-02-15")
    @ColumnDefault(value = "current_timestamp") private LocalDate niDate;
    // @OneToOne @JoinColumn(name="ni_nii_seq") private ScheduleImgInfoEntity scheduleImgInfoEntity;
    // @OneToOne @JoinColumn(name="ni_nii_seq") private ScheduleImgInfoEntity scheduleImgInfoEntity;
    // @Column(name="ni_mbi_seq") @Schema(description="회원 seq", example="1") private Long niMbiSeq;
    @JsonIgnore
    @Schema(description="공지사항 등록회원번호", example="1")
    @OneToOne @JoinColumn(name = "ni_mbi_seq") private MemberBasicInfoEntity memberInfo;
    // @Column(name="ni_nii_seq") @Schema(description="공지사항 사진번호 seq", example="2")private Long niNiiSeq;
    // @OneToMany(mappedBy = "noticeInfo") private NoticeImgInfoEntity NoticeImgInfo;
}