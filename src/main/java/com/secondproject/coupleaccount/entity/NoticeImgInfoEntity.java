package com.secondproject.coupleaccount.entity;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
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
@Table(name="notioce_img_info")
public class NoticeImgInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description="공지사항 이미지 번호", example="1")
    @Column(name="nii_seq") @JsonIgnore private Long niiSeq;
    @Schema(description="공지사항 이미지 이름", example="")
    @Column(name="nii_filename") @JsonIgnore private String niiFileName;
    @Schema(description="공지사항 이미지 uri", example="http://localhost:9090/api/notice/image")
    @Column(name="nii_uri") private String niiUri;
    // @Column(name="nii_ni_seq") private Long niiNiSeq;
    @OneToOne(cascade = CascadeType.ALL) @JoinColumn(name = "nii_ni_seq") NoticeInfoEntity noticeInfo;
    // @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = "nii_ni_seq") NoticeInfoEntity noticeInfo;
    @Schema(description="공지사항 이미지 등록회원 번호", example="1")
    @Column(name="nii_mbi_seq") private Long niiMbiSeq;
}
