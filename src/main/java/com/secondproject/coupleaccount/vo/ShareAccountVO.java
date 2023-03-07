package com.secondproject.coupleaccount.vo;

import java.time.LocalDate;

import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;

import lombok.Data;

@Data
public class ShareAccountVO {
private Long saiSeq;
private String accountName;
private LocalDate accountStartDay;
private String accountCode;

public ShareAccountVO(ShareAccountInfoEntity shareAccount)   
{
    this.accountName = shareAccount.getSaiName();
    this.accountStartDay = shareAccount.getSaiStartDay();
    this.accountCode = shareAccount.getSaiCode();
}

}
