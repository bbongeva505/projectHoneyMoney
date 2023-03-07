package com.secondproject.coupleaccount.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ScheduleImgInfoEntity;
import com.secondproject.coupleaccount.entity.ScheduleInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.repository.SchduleImgInfoRepository;
import com.secondproject.coupleaccount.repository.ScheduleInfoRepository;
import com.secondproject.coupleaccount.repository.ShareAccountInfoRepository;
import com.secondproject.coupleaccount.utils.CommonUtils;
import com.secondproject.coupleaccount.vo.schdule.SchedulInfoVO;
import com.secondproject.coupleaccount.vo.schdule.SchedulallVO;
import com.secondproject.coupleaccount.vo.schdule.ScheduleImgInfoVo;
import com.secondproject.coupleaccount.vo.schdule.ScheduleResponseVO;
import com.secondproject.coupleaccount.vo.schdule.ScheduleResponseVO1;
import com.secondproject.coupleaccount.vo.schdule.ScheduleUpdateVO;
import com.secondproject.coupleaccount.vo.schdule.schedualDetailResponseVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class SchedulInfoService {
        @Value("${file.image.schedule}") String schedul_img;
        private final MemberBasicInfoRepository memberRepo;
        private final ScheduleInfoRepository scheduleInforepository;
        private final SchduleImgInfoRepository Imgrepository;
        private final ScheduleImgService scheduleImg;
        private final ShareAccountInfoRepository sRepo;        
        private final AmazonS3Client amazonS3Client;

        @Value("${cloud.aws.s3.bucket}")
        
  private String bucketName;
        //스케줄 리스트
        public Map<String, Object> AllList(){          
                List<ScheduleInfoEntity> scheduleInfoEntities = scheduleInforepository.findAll();
                Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
                resultMap.put("status", true);
                resultMap.put("message", "캘린더 내용을 출력하였습니다.");
                resultMap.put("list", scheduleInfoEntities);
                resultMap.put("code", HttpStatus.OK);
                return resultMap;
        } 
        
        // 캘린더 삭제하기
        @Transactional
        public void scheduledelete(Long si_seq) {
                scheduleInforepository.deleteById(si_seq);
        }
        // 이미지 추가하기
        public ScheduleImgInfoVo addschedulInfo(@RequestPart(value = "file") MultipartFile file, Long mbiSeq,
                @RequestPart(value = "json") SchedulInfoVO data) throws Exception {
                if (memberRepo.countByMbiSeq(mbiSeq) == 0) {
                        ScheduleImgInfoVo result = ScheduleImgInfoVo.builder().status(false).message("존재하지 않는 회원번호입니다.")
                                .build();
                        return result;
                } else if (data.getSiMemo() == null) {
                        ScheduleImgInfoVo result = ScheduleImgInfoVo.builder().status(false).message("메모를 입력해 주세요")
                                .build();
                        return result;
                }

                // Path folderLocation = Paths.get(schedul_img);
                ScheduleImgInfoEntity data1 = null;
                // if (file != null) {
                //         String originFileName = file.getOriginalFilename();
                //         if (!originFileName.equals("")) {
                //                 String[] split = originFileName.split("\\.");
                //                 String ext = split[split.length - 1];
                //                 String filename = "";
                //                 for (int i = 0; i < split.length - 1; i++) {
                //                         filename += split[i];
                //                 }
                //                 String saveFileName = "account" + "-";
                //                 Calendar c = Calendar.getInstance();
                //                 saveFileName += c.getTimeInMillis() + "." + ext;
                //                 Path targetFile = folderLocation.resolve(saveFileName);
                //                 try {
                //                         Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
                //                 } catch (Exception e) {
                //                         e.printStackTrace();
                //                 }
                //                 data1 = new ScheduleImgInfoEntity();
                //                 data1.setSiiFileName(saveFileName);
                //                 data1.setSiiUri(filename);
                //                 scheduleImg.scheulIngInfo(data1);
                //                 // Imgrepository.save(data1);
                //         }
                // }

                
        // validateFileExists(file);

        String fileName = CommonUtils.buildFileName(file.getOriginalFilename());
    
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
    
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
          } catch (IOException e) {
            throw null;
          }
          data1 = new ScheduleImgInfoEntity();
                          data1.setSiiFileName(fileName);
                          data1.setSiiUri(fileName);
                          scheduleImg.scheulIngInfo(data1);

                ScheduleInfoEntity entity = new ScheduleInfoEntity();
                entity.setSiStartDate(data.getSiStartDate());
                entity.setSiEndDate(data.getSiEndDate());
                entity.setSiMemo(data.getSiMemo());
                entity.setMemberbasicinfo(memberRepo.findById(mbiSeq).get());
                entity.setScheduleimg(data1);
                System.out.println(entity);
                scheduleInforepository.save(entity);
                ScheduleImgInfoVo result = ScheduleImgInfoVo.builder().status(true).message("스케줄이 등록되었습니다.").build();
                return result;
        }

        // 스케줄 수정
        public ScheduleImgInfoVo updateSchedue(@RequestPart(value = "file") MultipartFile file, Long mbiSeq,
                @RequestPart(value = "json") ScheduleUpdateVO data, Long siSeq) {
                if (scheduleInforepository.countBySiSeqAndSiMiSeq(siSeq,mbiSeq) == 0) {
                        ScheduleImgInfoVo result = ScheduleImgInfoVo.builder().status(false)
                                .message("수정권한이 없거나 알맞지 않은 수정번호입니다.").build();
                        return result;
                }
                ScheduleInfoEntity entity = scheduleInforepository.findById(siSeq).get();
                if (data.getUpdateStartDate() == null) {
                        data.setUpdateStartDate(entity.getSiStartDate());
                }
                if (data.getUpdateEndDate() == null) {
                        data.setUpdateEndDate(entity.getSiEndDate());
                }
                if (data.getSiMemo() == null) {
                        data.setSiMemo(entity.getSiMemo());
                }
                // Path folderLocation = Paths.get(schedul_img);
                ScheduleImgInfoEntity data1 = null;
                // if (file != null) { // 파일이 체크되어있고
                //         String originFileName = file.getOriginalFilename();
                //         // 파일이 체크만 되어있고 실제 파일이 없을 수도 있다. 파일이 진짜 있으면 
                //         if (!originFileName.equals("")) {
                //                 String[] split = originFileName.split("\\.");
                //                 String ext = split[split.length - 1];
                //                 String filename = "";
                //                 for (int i = 0; i < split.length - 1; i++) {
                //                         filename += split[i];
                //                         // 파일네임 생성
                //                 }
                //                 if (entity.getScheduleimg() != null) {
                //                         String deleteFileName = entity.getScheduleimg().getSiiFileName();
                //                         Path targetDeleteFile = folderLocation.resolve(deleteFileName);
                //                         try {
                //                                 Files.delete(targetDeleteFile);
                //                         } catch (Exception e) {
                //                                 e.printStackTrace();
                //                         }
                //                         String saveFileName = "account" + "-";
                //                         Calendar c = Calendar.getInstance();
                //                         saveFileName += c.getTimeInMillis() + "." + ext;
                //                         Path targetFile = folderLocation.resolve(saveFileName);
                //                         try {
                //                                 Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
                //                         } catch (Exception e) {
                //                                 e.printStackTrace();
                //                         }
                //                         data1 = entity.getScheduleimg();
                //                         data1.setSiiFileName(saveFileName);
                //                         data1.setSiiUri(filename);
                //                         scheduleImg.scheulIngInfo(data1);
                //                 } else {
                //                         String saveFileName = "account" + "-";
                //                         Calendar c = Calendar.getInstance();
                //                         saveFileName += c.getTimeInMillis() + "." + ext;
                //                         Path targetFile = folderLocation.resolve(saveFileName);
                //                         try {
                //                                 Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
                //                         } catch (Exception e) {
                //                                 e.printStackTrace();
                //                         }
                //                         data1 = new ScheduleImgInfoEntity();
                //                         data1.setSiiFileName(saveFileName);
                //                         data1.setSiiUri(filename);
                //                         scheduleImg.scheulIngInfo(data1);
                //                 }
                //         }
                // } else {
                //         data1 = entity.getScheduleimg();
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
                  data1 = new ScheduleImgInfoEntity();
                  data1.setSiiFileName(fileName);
                  data1.setSiiUri(fileName);
                  scheduleImg.scheulIngInfo(data1);



                entity.setSiStartDate(data.getUpdateStartDate());
                entity.setSiEndDate(data.getUpdateEndDate());
                entity.setSiMemo(data.getSiMemo());
                entity.setScheduleimg(data1);
                scheduleInforepository.save(entity);
                ScheduleImgInfoVo result = ScheduleImgInfoVo.builder().status(true).message("스케줄이 수정되었습니다.")
                        .build();
                return result;
        }


        // 스케줄 이미지 삭제
        public ScheduleImgInfoVo deleteSchedule(Long siSeq) {
                if (scheduleInforepository.countBySiSeq(siSeq) == 0) {
                        ScheduleImgInfoVo result = ScheduleImgInfoVo.builder().status(false).message("존재하지 않는 스케줄 정보 번호입니다.")
                        .build();
                return result;
                }
                ScheduleInfoEntity entity = scheduleInforepository.findById(siSeq).get();
                if (entity.getScheduleimg() == null) {
                        ScheduleImgInfoVo result = ScheduleImgInfoVo.builder().status(false).message("이미지가 존재하지 않습니다.")
                                .build();
                        return result;
                }
                Path folderLocation = Paths.get(schedul_img);
                String deleteFileName = entity.getScheduleimg().getSiiFileName();
                Path targetDeleteFile = folderLocation.resolve(deleteFileName);
                try {
                        Files.delete(targetDeleteFile);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        
                Imgrepository.delete(entity.getScheduleimg());
                entity.setScheduleimg(null);
                scheduleInforepository.save(entity);
                ScheduleImgInfoVo result = ScheduleImgInfoVo.builder().status(true).message("이미지가 삭제되었습니다.")
                        .build();
                return result;
        }


        
        // 월별 리스트 조회
        public ScheduleResponseVO getCoupleSchedule(Long saiSeq, Integer year, Integer month) {
                ShareAccountInfoEntity share = sRepo.findById(saiSeq).orElse(null);
                if(share == null) {
                        ScheduleResponseVO.builder().status(false).message("공유번호가 존재하지 않습니다.").scheduleList(null).build();   
                }
                List<MemberBasicInfoEntity> member = memberRepo.findByShareAccount(share);
                List<Long> seqs = new ArrayList<>();
                for(MemberBasicInfoEntity m : member){
                        seqs.add(m.getMbiSeq());
                } 
                LocalDate firstDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        // String firstDateString = firstDate.format(DateTimeFormatter.ISO_DATE);
        // LocalDate start = LocalDate.parse(firstDate.format(DateTimeFormatter.ISO_DATE));
        LocalDate lastDate = firstDate.with(TemporalAdjusters.lastDayOfMonth());
        // LocalDate end = LocalDate.parse(lastDate.format(DateTimeFormatter.ISO_DATE));
        if(scheduleInforepository.findByCoupleScheduleInfo(member, firstDate, lastDate) == null){
                ScheduleResponseVO.builder().status(false).message("x").scheduleList(null).build();
        }
        ScheduleResponseVO result = ScheduleResponseVO.builder().status(true).message("ok")
        .scheduleList(scheduleInforepository.findByCoupleScheduleInfo(member, firstDate, lastDate)).build();
        return result;
        }


        // 월별
        public ScheduleResponseVO1 getCoupleSchedule1(Long saiSeq1, Integer year1, Integer month1) {
                ShareAccountInfoEntity share = sRepo.findById(saiSeq1).orElse(null);
                if(share == null) {
                        ScheduleResponseVO.builder().status(false).message("스케줄이 존재하지 않습니다.").scheduleList(null).build();   
                }
                List<MemberBasicInfoEntity> member = memberRepo.findByShareAccount(share);
                List<Long> seqs = new ArrayList<>();
                for(MemberBasicInfoEntity m : member){
                        seqs.add(m.getMbiSeq());
                } 
                LocalDate firstDate = LocalDate.of(year1, month1, 1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDate = firstDate.with(TemporalAdjusters.lastDayOfMonth());
        if(scheduleInforepository.findByCoupleScheduleInfo1(member, firstDate, lastDate) == null){
                ScheduleResponseVO1.builder().status(false).message("x").scheduleList1(null).build();
        }
        ScheduleResponseVO1 result = ScheduleResponseVO1.builder().status(true).message("ok")
        .scheduleList1(scheduleInforepository.findByCoupleScheduleInfo1(member, firstDate, lastDate)).build();
        return result;
        }

        public schedualDetailResponseVO getDeatailSchedule(Long saiSeq) {
                ScheduleInfoEntity schedule =  scheduleInforepository.findBySiSeq(saiSeq);               

                if(schedule.getScheduleimg() == null){
                        schedualDetailResponseVO response = new schedualDetailResponseVO(schedule, null);
                        return response;
                }                 
                                           
                ScheduleImgInfoEntity imgEntity = schedule.getScheduleimg();
                schedualDetailResponseVO response = new schedualDetailResponseVO(schedule, imgEntity);
                return response;
        }

}
