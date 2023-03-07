package com.secondproject.coupleaccount.service;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.secondproject.coupleaccount.api.config.JwtProperties;
import com.secondproject.coupleaccount.api.config.JwtUtil;
import com.secondproject.coupleaccount.entity.BackgroundImgInfoEntity;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;
import com.secondproject.coupleaccount.entity.TokenBlackList;
import com.secondproject.coupleaccount.repository.BackgroundImgInfoRepository;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.repository.ShareAccountInfoRepository;
import com.secondproject.coupleaccount.repository.TokenBlackListRepository;
import com.secondproject.coupleaccount.utils.CommonUtils;
import com.secondproject.coupleaccount.vo.LoginMemberVO;
import com.secondproject.coupleaccount.vo.MemberBasicAccountInfoVO;
import com.secondproject.coupleaccount.vo.MemberInfoChangeVO;
import com.secondproject.coupleaccount.vo.MemberJoinVo;
import com.secondproject.coupleaccount.vo.member.FindPwdResponseVO;
import com.secondproject.coupleaccount.vo.member.FindPwdVO;
import com.secondproject.coupleaccount.vo.member.LoginResponseInfoVO;
import com.secondproject.coupleaccount.vo.member.LoginResponseVO;
import com.secondproject.coupleaccount.vo.member.MemberDuplicatedResponseVO;
import com.secondproject.coupleaccount.vo.member.MemberDuplicatedVO;
import com.secondproject.coupleaccount.vo.member.MemberJoinResponse;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.secondproject.coupleaccount.utils.CommonUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final MemberBasicInfoRepository memberBasicInfoRepository;
    private final JwtUtil jwtUtil;
    private final TokenBlackListRepository tokenBlackListRepository;
    private final ShareAccountInfoRepository shareAccountInfoRepository;
    private final MailService eMailService;
    private final AmazonS3Client amazonS3Client;
    private final BackgroundImgInfoRepository backgroundImgInfoRepository;

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
          throw null;
        }
      }

    public LoginResponseVO loginUser(LoginMemberVO data){

        MemberBasicInfoEntity memberBasicInfoEntity = memberBasicInfoRepository.findByMbiBasicEmailAndMbiPassword(data.getEmail(), data.getPassword());

        if(memberBasicInfoEntity == null) { 

            LoginResponseVO response = LoginResponseVO.builder().status(false).Authorization(null).message("아이디나 비밀번호를 확인해주세요")
            .build();
            return response;
        }    
        

        LoginResponseVO response = LoginResponseVO.builder().status(true).Authorization(JwtProperties.TOKEN_PREFIX + jwtUtil.create(memberBasicInfoEntity.getMbiSeq()))
        .message("로그인 완료")
        .build();
        return response;
    }

    @Transactional
    public void blackListToken(String token) {
        long expireTime = jwtUtil.getExpireTime(token);
        
        // tokenBlackListRepository.save(new Tokenblacklist(token, expireTime));
        // tokenBlackListRepository.save(new TokenBlackList(token, expireTime));
    }

    public Map<String, Object> memberList() {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        
        resultMap.put("list", memberBasicInfoRepository.findAll());

        return resultMap;
    }



    public Boolean passwordPattern(String password) {
        String pwdPattern =  "^[a-zA-Z\\d`~!@#$%^&*()-_=+]{6,20}$"; // 비밀번호는 영문과 숫자를 포함하여 6자 이상~20자 미만이여야 합니다.

        if(!Pattern.matches(pwdPattern, password)) {
            return false;
        }
        else {
            return true;
        }
    }

    public MemberDuplicatedResponseVO memberDuplicatedCheck(MemberDuplicatedVO data) {
        MemberDuplicatedResponseVO response = new MemberDuplicatedResponseVO();

        if(duplicatedEmail(data.getUserEmail()) == true) {  
            response.setStatus(false);
            response.setMessage("회원가입 중복");
            return response;
        }
        else {
            response.setStatus(true);
            response.setMessage("이메일 사용가능");            
            return response;
        }
    }

    @Value("${file.image.background}") String background_img;

    @Autowired BackgroundImgService backgroundImgService;
 
    public Boolean duplicatedEmail(String email) { // 회원가입 이메일 중복검사
        if(memberBasicInfoRepository.findByMbiBasicEmail(email).isPresent() == true) {            
            return true;        
        }
        else {
            return false;
        }
    }



    public MemberJoinResponse memberTotalJoin1(MemberJoinVo data, MultipartFile file) {      
        MemberJoinResponse response = new MemberJoinResponse();
        
        ShareAccountInfoEntity shareAccountInfoEntity = shareAccountInfoRepository.findBySaiCode(data.getAccountNumber());

        
        if(duplicatedEmail(data.getMbiBasicEmail()) == true) {  
            System.out.println((memberBasicInfoRepository.findByMbiBasicEmail(data.getMbiBasicEmail())));
            response.setStatus(false);
            response.setMessage("회원가입 중복");
            return response;
        }

        else {

        if(passwordPattern(data.getPassword()) == false) {
            System.out.println(passwordPattern(data.getPassword()));
            response.setStatus(false);
            response.setMessage("비밀번호는 영문자+숫자 조합의 6자~20자 형식이여야 합니다");
            return response;
        }

        if(shareAccountInfoEntity == null) {
            response.setStatus(false);
            response.setMessage("해당하는 데이트 통장 정보가 없습니다");
            return response;
        }

        if(file == null) {
                 MemberBasicInfoEntity memberBasicInfoEntity = MemberBasicInfoEntity.builder().mbiBasicEmail(data.getMbiBasicEmail()).mbiBirth(data.getMbiBrith())
                                                        .mbiGender(data.getGender()).mbiName(data.getName()).mbiNickName(data.getNickName()).mbiPassword(data.getPassword())
                                                        .mbiStartDay(data.getMbiStartDay())
                                                        .shareAccount(shareAccountInfoEntity)
                                                        .backgroundImgInfoEntity(null)                                                        
                                                        .build();
                memberBasicInfoRepository.save(memberBasicInfoEntity); 
                response.setStatus(true);
                response.setMessage("회원가입 완료");
                return response;
        }

        // Path folderLocation = Paths.get(background_img);
        // String originFileName = file.getOriginalFilename();
        // String [] split  = originFileName.split("\\.");
        // String ext = split[split.length-1];
        // String filename = "";
        // for(int i= 0; i < split.length-1; i++){
        //     filename += split[i];
        // }
        // String saveFileName = "background"+"-";
        // Calendar c = Calendar.getInstance();
        // saveFileName += c.getTimeInMillis()+"."+ext;
        // Path targetFile = folderLocation.resolve(saveFileName);
        // try{
        //     Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        // }
        // catch(Exception e){
        //     e.printStackTrace();
        // }
        
        validateFileExists(file);

        String fileName = CommonUtils.buildFileName(file.getOriginalFilename());
    
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
    
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
          } catch (IOException e) {
            throw null;
          }

        BackgroundImgInfoEntity data1 = new BackgroundImgInfoEntity();
        data1.setBiiFileName(fileName);
        data1.setBiiUri(fileName);
        data1.setBiiMbiSeq(null);

        backgroundImgInfoRepository.save(data1);

        // BackgroundImgInfoEntity data1 = BackgroundImgInfoEntity.builder().biiFileName(fileName).biiUri(fileName).build();


        // data.setBiiUri("http://localhost:9090/background/img/"+filename);
        // backgroundImgService.addBackgroundImage(data1);


        MemberBasicInfoEntity memberBasicInfoEntity = MemberBasicInfoEntity.builder().mbiBasicEmail(data.getMbiBasicEmail()).mbiBirth(data.getMbiBrith())
                                                        .mbiGender(data.getGender()).mbiName(data.getName()).mbiNickName(data.getNickName()).mbiPassword(data.getPassword())
                                                        .mbiStartDay(data.getMbiStartDay())
                                                        .shareAccount(shareAccountInfoEntity)
                                                        .backgroundImgInfoEntity(data1)                                                        
                                                        .build();

        memberBasicInfoRepository.save(memberBasicInfoEntity);   
        
        response.setStatus(true);
        response.setMessage("회원가입 완료");
        return response;
    }
    
    }

    public MemberBasicAccountInfoVO memberInfoView(Long miSeq){        
        MemberBasicInfoEntity memberBasicInfo = memberBasicInfoRepository.findByMbiSeq(miSeq);

        MemberBasicAccountInfoVO memberBasicAccountInfoVO = new MemberBasicAccountInfoVO(memberBasicInfo, memberBasicInfo.getShareAccount(), memberBasicInfo.getBackgroundImgInfoEntity());   

        return memberBasicAccountInfoVO;
    }

    public LoginResponseInfoVO loginMemberResponse(LoginMemberVO data) {
        MemberBasicInfoEntity memberBasicInfoEntity = memberBasicInfoRepository.findByMbiBasicEmailAndMbiPassword(data.getEmail(), data.getPassword());

        String tokenAuth =  JwtProperties.TOKEN_PREFIX + jwtUtil.create(memberBasicInfoEntity.getMbiSeq());

        List<MemberBasicInfoEntity> shareAccountMembers = memberBasicInfoRepository.findByShareAccount(memberBasicInfoEntity.getShareAccount());
     


        if(shareAccountMembers.size() == 1) {
            LoginResponseInfoVO loginResponseInfoVO =
            new LoginResponseInfoVO(memberBasicInfoEntity, null, null, null , null, null, "상대 가입 대기중", false);
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaacheck");
            return loginResponseInfoVO;
        }

        else if(shareAccountMembers.get(0).getMbiGender() == memberBasicInfoEntity.getMbiGender()) {
            MemberBasicInfoEntity othermember = shareAccountMembers.get(1); // 연결된 딴사람의 memberEntity            
            BackgroundImgInfoEntity otherImage = othermember.getBackgroundImgInfoEntity();
            LoginResponseInfoVO loginResponseInfoVO = 
            new LoginResponseInfoVO(memberBasicInfoEntity, memberBasicInfoEntity.getShareAccount(), memberBasicInfoEntity.getBackgroundImgInfoEntity(), othermember, otherImage
            ,tokenAuth, "로그인완료", true);
            return loginResponseInfoVO;            
        }

        else if(shareAccountMembers.get(0).getMbiGender() != memberBasicInfoEntity.getMbiGender()) {
            MemberBasicInfoEntity othermember = shareAccountMembers.get(0);
            BackgroundImgInfoEntity otherImage = othermember.getBackgroundImgInfoEntity();            
            LoginResponseInfoVO loginResponseInfoVO = 
            new LoginResponseInfoVO(memberBasicInfoEntity, memberBasicInfoEntity.getShareAccount(), memberBasicInfoEntity.getBackgroundImgInfoEntity(), othermember, otherImage
            ,tokenAuth, "로그인완료", true);
            return loginResponseInfoVO;            
        }

        
        LoginResponseInfoVO response = new LoginResponseInfoVO(null, null, null, null, null, null, null, null);
        return response;
        // LoginResponseInfoVO loginResponseInfoVO = new LoginResponseInfoVO(memberBasicInfoEntity, memberBasicInfoEntity.getShareAccount(), memberBasicInfoEntity.getBackgroundImgInfoEntity());
    }



    public Map<String, Object> memberInfoChange(Long mbiSeq, MemberInfoChangeVO data, MultipartFile file) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        MemberBasicInfoEntity memberBasicInfo = memberBasicInfoRepository.findByMbiSeq(mbiSeq);

        if(memberBasicInfo.getBackgroundImgInfoEntity() == null) {
            String fileName = CommonUtils.buildFileName(file.getOriginalFilename());
    
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
        
            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
              } catch (IOException e) {
                throw null;
              }
        
            // String saveFileName = "background"+"-";
            // Calendar c = Calendar.getInstance();
            // saveFileName += c.getTimeInMillis()+"."+ext;

            // Path targetFile = folderLocation.resolve(saveFileName);
            // try{
            //     Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            //     }
            // catch(Exception e){
            //     e.printStackTrace();
            // }

            BackgroundImgInfoEntity data1 = new BackgroundImgInfoEntity();
            data1.setBiiFileName(fileName);
            data1.setBiiUri(fileName);
            data1.setBiiMbiSeq(null);
            memberBasicInfo.setBackgroundImgInfoEntity(data1);
        // data.setBiiUri("http://localhost:9090/background/img/"+filename);
            backgroundImgService.addBackgroundImage(data1);
            resultMap.put("status", true);
            resultMap.put("message", "사용자 이미지 등록완료");        
            return resultMap;
        }

        if(data == null && memberBasicInfo.getBackgroundImgInfoEntity() != null) {
        //     Path folderLocation = Paths.get(background_img);
        //     String originFileName = file.getOriginalFilename();
        //     String [] split  = originFileName.split("\\.");
        //     String ext = split[split.length-1];
        //     String filename = "";
        //     for(int i= 0; i < split.length-1; i++){
        //     filename += split[i];
        // }
        //     String saveFileName = "background"+"-";
        //     Calendar c = Calendar.getInstance();
        //     saveFileName += c.getTimeInMillis()+"."+ext;

        //     String deleteFileName = memberBasicInfo.getBackgroundImgInfoEntity().getBiiFileName();
        //     Path targetDeleteFile = folderLocation.resolve(deleteFileName);

        //     try{
        //         Files.delete(targetDeleteFile);
        //     }
        //     catch(Exception e) {
        //         e.printStackTrace();
        //     }

        //     Path targetFile = folderLocation.resolve(saveFileName);
        //     try{
        //         Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        //         }
        //     catch(Exception e){
        //         e.printStackTrace();
        //     }

            
        String fileName = CommonUtils.buildFileName(file.getOriginalFilename());
    
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
    
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
          } catch (IOException e) {
            throw null;
          }



            BackgroundImgInfoEntity data1 = memberBasicInfo.getBackgroundImgInfoEntity();
            data1.setBiiFileName(fileName);
            data1.setBiiUri(fileName);
            data1.setBiiMbiSeq(null);
            memberBasicInfo.setBackgroundImgInfoEntity(data1);
        // data.setBiiUri("http://localhost:9090/background/img/"+filename);
            backgroundImgService.addBackgroundImage(data1);
            resultMap.put("status", true);
            resultMap.put("message", "사용자 이미지만 변경완료");        
            return resultMap;
        }

        
       
        else if(file == null) {
            if(data.getName() == null) {
                data.setName(memberBasicInfo.getMbiName());
            }
    
            if(data.getPassword() == null){
                data.setPassword(memberBasicInfo.getMbiPassword());
            }
    
            if(data.getNickName() == null){
                data.setNickName(memberBasicInfo.getMbiNickName());
            }
    
            memberBasicInfo.setMbiName(data.getName());
            memberBasicInfo.setMbiPassword(data.getPassword());       
            memberBasicInfo.setMbiNickName(data.getNickName());
    
            memberBasicInfoRepository.save(memberBasicInfo);  
            resultMap.put("status", true);
            resultMap.put("message", "사용자 정보만 변경완료");
            return resultMap;
        }
        
        if(data.getName() == null) {
            data.setName(memberBasicInfo.getMbiName());
        }

        if(data.getPassword() == null){
            data.setPassword(memberBasicInfo.getMbiPassword());
        }

        if(data.getNickName() == null){
            data.setNickName(memberBasicInfo.getMbiNickName());
        }

        memberBasicInfo.setMbiName(data.getName());
        memberBasicInfo.setMbiPassword(data.getPassword());       
        memberBasicInfo.setMbiNickName(data.getNickName());

        memberBasicInfoRepository.save(memberBasicInfo);       

        
        // Path folderLocation = Paths.get(background_img);
        // String originFileName = file.getOriginalFilename();
        // String [] split  = originFileName.split("\\.");
        // String ext = split[split.length-1];
        // String filename = "";
        // for(int i= 0; i < split.length-1; i++){
        //     filename += split[i];
        // }
        // String saveFileName = "background"+"-";
        // Calendar c = Calendar.getInstance();
        // saveFileName += c.getTimeInMillis()+"."+ext;
        // Path targetFile = folderLocation.resolve(saveFileName);

        // String deleteFileName = memberBasicInfo.getBackgroundImgInfoEntity().getBiiFileName();
        // Path targetDeleteFile = folderLocation.resolve(deleteFileName);

        // try{
        //     Files.delete(targetDeleteFile);
        // }
        // catch(Exception e) {
        //     e.printStackTrace();
        // }

        // try{
        //     Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        // }
        // catch(Exception e){
        //     e.printStackTrace();
        // }

        
        String fileName = CommonUtils.buildFileName(file.getOriginalFilename());
    
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
    
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
          } catch (IOException e) {
            throw null;
          }

        BackgroundImgInfoEntity data1 = memberBasicInfo.getBackgroundImgInfoEntity();
        data1.setBiiFileName(fileName);
        data1.setBiiUri(fileName);
        data1.setBiiMbiSeq(null);
        // data.setBiiUri("http://localhost:9090/background/img/"+filename);
        backgroundImgService.addBackgroundImage(data1);    

        resultMap.put("status", true);
        resultMap.put("message", "사용자 이미지, 정보 변경완료");        

        return resultMap;
    }

    public FindPwdResponseVO loginUser(FindPwdVO data) throws Exception{
        if(memberBasicInfoRepository.findByMbiNameAndMbiBirthAndMbiBasicEmail(data.getName(), data.getBirth(), data.getEmail()) == null) {
            FindPwdResponseVO response = new FindPwdResponseVO(false, "해당하는 회원이 존재하지 않습니다" );
            return response;
        }

        if(memberBasicInfoRepository.findByMbiNameAndMbiBirthAndMbiBasicEmail(data.getName(), data.getBirth(), data.getEmail()) != null) {
            MemberBasicInfoEntity memberEntity = memberBasicInfoRepository.findByMbiNameAndMbiBirthAndMbiBasicEmail(data.getName(), data.getBirth(), data.getEmail());
            String code = eMailService.sendSimpleMessage(memberEntity.getMbiBasicEmail());
            memberEntity.setMbiPassword(code);
            memberBasicInfoRepository.save(memberEntity);           

            FindPwdResponseVO response = new FindPwdResponseVO(true, "임시 비밀번호 발송완료");
            return response;
        }

        FindPwdResponseVO response = new FindPwdResponseVO(false, "해당하는 회원이 없음");
        return response;
    }
}
