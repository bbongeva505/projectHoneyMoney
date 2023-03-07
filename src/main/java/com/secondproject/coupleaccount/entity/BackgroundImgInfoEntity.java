package com.secondproject.coupleaccount.entity;
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
@Table(name="background_img_info")
public class BackgroundImgInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bii_seq") private Long biiSeq;
    @Column(name="bii_filename") private String biiFileName ;
    @Column(name="bii_uri") private String biiUri;
    @Column(name="bii_mbi_seq") private Long biiMbiSeq;
}
