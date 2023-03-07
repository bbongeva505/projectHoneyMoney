package com.secondproject.coupleaccount.vo.member;
import java.time.LocalDate;
import java.time.Period;
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
public class LoginResponseInfoVO {
    // @Schema(description = "멤버 고유번호", example = "1")
    // private Long memberSeq;
    // private String mbiPassword;
    @Schema(description = "멤버 고유번호", example = "1")
    private Long mbiSeq;
    @Schema(description = "멤버 이메일(아이디)", example = "1234@1234")
    private String mbiBasicEmail;
    private Integer gender;
    private String name;
    private String otherName;
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    // private LocalDate startDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String nickName;
    private String otherNickName;
    @Schema(description = "공유계정 이름", example = "우리통장")
    private String shareAccountName;
    private String shareAccountCode;
    private Integer loveDay;
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    // @Schema(description = "공유계정에 설정한 기념일", example = "2022-01-12")
    // private LocalDate shareAccountStartDay;
    // @Schema(description = "공유계정 고유코드", example = "VDcEKfGW6F")
    // private String shareAccountCode;
    // private String memberImgName;
    private String memberImgURL;
    private String otherMemberImgURL;
    @JsonProperty("Authentication")
    private String Authentication;
    private String message;
    @JsonProperty("status")
    private Boolean status;
    // , ShareAccountInfoEntity shareAccountInfoEntity, BackgroundImgInfoEntity backgroundImgInfoEntity
    public LoginResponseInfoVO(MemberBasicInfoEntity memberBasicInfo, ShareAccountInfoEntity shareAccountInfoEntity, BackgroundImgInfoEntity backgroundImgInfoEntity
    , MemberBasicInfoEntity otherMember, BackgroundImgInfoEntity otherMemberImage, String auth, String message, Boolean status) {
        // ShareAccountInfoEntity shareAccountInfoEntity = memberBasicInfo.getShareAccount();
        LocalDate currentDate = LocalDate.now();

        if(shareAccountInfoEntity == null) {
            this.mbiSeq = memberBasicInfo.getMbiSeq();
            this.name = memberBasicInfo.getMbiName();
            this.mbiBasicEmail = memberBasicInfo.getMbiBasicEmail();
            this.gender = memberBasicInfo.getMbiGender();
            this.birth = memberBasicInfo.getMbiBirth();
            this.nickName = memberBasicInfo.getMbiNickName();
            this.message = message;
            this.status = false;
            this.shareAccountName = memberBasicInfo.getShareAccount().getSaiName();
            this.shareAccountCode = memberBasicInfo.getShareAccount().getSaiCode();
            return;
        }
        this.mbiSeq = memberBasicInfo.getMbiSeq();

        Period period = Period.between(shareAccountInfoEntity.getSaiStartDay(), currentDate);
        this.loveDay = period.getDays() + (period.getYears() * 365);

        this.Authentication = auth;
        this.message = message;
        this.name = memberBasicInfo.getMbiName();
        this.otherName = otherMember.getMbiName();
        this.mbiBasicEmail = memberBasicInfo.getMbiBasicEmail();
        // this.memberSeq = memberBasicInfo.getMbiSeq();
        this.gender = memberBasicInfo.getMbiGender();
        // this.startDay = memberBasicInfo.getMbiStartDay();
        this.birth = memberBasicInfo.getMbiBirth();
        this.nickName = memberBasicInfo.getMbiNickName();
        this.otherNickName = otherMember.getMbiNickName();
        this.shareAccountName = memberBasicInfo.getShareAccount().getSaiName();
        this.shareAccountCode = memberBasicInfo.getShareAccount().getSaiCode();
        this.status = status;
        // this.shareAccountStartDay = shareAccountInfoEntity.getSaiStartDay();
        // this.shareAccountCode = shareAccountInfoEntity.getSaiCode();
        // this.memberImgName = backgroundImgInfoEntity.getBiiFileName();
        if(backgroundImgInfoEntity != null) {
            this.memberImgURL = backgroundImgInfoEntity.getBiiUri();
        }
        if(otherMemberImage != null) {
            this.otherMemberImgURL = otherMemberImage.getBiiUri();
        }
    }
}