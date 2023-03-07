package com.secondproject.coupleaccount.vo;

import com.secondproject.coupleaccount.entity.BackgroundImgInfoEntity;

import lombok.Data;

@Data
public class BackGroundImgVO {
// private Long biiSeq;
private String fileName;
private String uri;
// private Long biiMbiSeq;

public BackGroundImgVO(BackgroundImgInfoEntity backGroundImage) {
    this.fileName =backGroundImage.getBiiFileName();
    this.uri = backGroundImage.getBiiUri();    
}
}
