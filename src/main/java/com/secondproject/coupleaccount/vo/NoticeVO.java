package com.secondproject.coupleaccount.vo;

import com.secondproject.coupleaccount.entity.NoticeInfoEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeVO {
  @Schema(description="메모내역", example="여행")
  private String memo;
  @Schema(description="공지사항 이미지 번호", example="1")
  private Long niNiiSeq;
  @Schema(description="공지사항 작성자 번호", example="200")
  private Long niMbiSeq;


  // public NoticeVO(NoticeInfoEntity data){
  //   this.memo = data.getNiMemo();
  //   this.niNiiSeq = data.getNiNiiSeq();
  //   this.niMbiSeq = data.getNiMbiSeq();
 
    // this.niNiiSeq = data.getNiNiiSeq();
    // this.niMbiSeq = data.getNiMbiSeq();
  }

