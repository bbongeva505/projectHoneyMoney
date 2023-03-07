package com.secondproject.coupleaccount.vo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.secondproject.coupleaccount.entity.BackgroundImgInfoEntity;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberBasicAccountInfoVO {
    @Schema(description = "멤버 고유번호", example = "1")    
    private Long memberSeq;
    // private String mbiPassword;
    @Schema(description = "멤버 이메일(아이디)", example = "1234@1234")   
    private String mbiBasicEmail;   
    private Integer gender;    
    private String name; 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")    
    private LocalDate startDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")    
    private LocalDate birth;   
    private String nickName;
    @Schema(description = "공유계정 이름", example = "우리통장")
    private String shareAccountName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "공유계정에 설정한 기념일", example = "2022-01-12")   
    private LocalDate shareAccountStartDay;
    @Schema(description = "공유계정 고유코드", example = "VDcEKfGW6F")   
    private String shareAccountCode;
    private String memberImgName;    
    private String memberImgURL;
               

    // , ShareAccountInfoEntity shareAccountInfoEntity, BackgroundImgInfoEntity backgroundImgInfoEntity

    public MemberBasicAccountInfoVO(MemberBasicInfoEntity memberBasicInfo, ShareAccountInfoEntity shareAccountInfoEntity, 
    BackgroundImgInfoEntity backgroundImgInfoEntity) {
        // ShareAccountInfoEntity shareAccountInfoEntity = memberBasicInfo.getShareAccount();        
        this.name = memberBasicInfo.getMbiName();
        this.mbiBasicEmail = memberBasicInfo.getMbiBasicEmail();
        this.memberSeq = memberBasicInfo.getMbiSeq();
        this.gender = memberBasicInfo.getMbiGender();
        this.startDay = memberBasicInfo.getMbiStartDay();
        this.birth = memberBasicInfo.getMbiBirth();
        this.nickName = memberBasicInfo.getMbiNickName();
        this.shareAccountName = shareAccountInfoEntity.getSaiName();
        this.shareAccountStartDay = shareAccountInfoEntity.getSaiStartDay();
        this.shareAccountCode = shareAccountInfoEntity.getSaiCode();   

        if(backgroundImgInfoEntity != null) {
            this.memberImgURL = "/api/background/img/" + backgroundImgInfoEntity.getBiiUri();
            this.memberImgName = backgroundImgInfoEntity.getBiiFileName();

        }            
    }
}
