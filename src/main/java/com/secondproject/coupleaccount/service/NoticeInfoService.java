package com.secondproject.coupleaccount.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.NoticeImgInfoEntity;
import com.secondproject.coupleaccount.entity.NoticeInfoEntity;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.repository.NoticeImgInfoRepository;
import com.secondproject.coupleaccount.repository.NoticeInfoRepository;
import com.secondproject.coupleaccount.utils.CommonUtils;
// import com.secondproject.coupleaccount.repository.NoticeInfoRepository;
// import com.secondproject.coupleaccount.repository.NoticeListRepository;
import com.secondproject.coupleaccount.vo.NoticeVO;
import com.secondproject.coupleaccount.vo.notice.NoticeResponseVO;
import com.secondproject.coupleaccount.vo.notice.NoticeUploadVO;
import com.secondproject.coupleaccount.vo.notice.NoticeUriResponseVO;
import com.secondproject.coupleaccount.vo.notice.ResponseVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeInfoService {
  private final NoticeInfoRepository noticeInfoRepository;
  // private final NoticeAddRepository noticeAddRepository;
  // private final NoticeListRepository noticeListRepository;
  private final NoticeImgInfoRepository noticeImgInfoRepository;
  private final MemberBasicInfoRepository memberBasicInfoRepository;
  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;
  
  // public Map<String,Object>noticeInfoAdd(NoticeInfoEntity data){
  //   Map<String,Object> map = new LinkedHashMap<String,Object>();
  //   noticeInfoRepository.save(data);
  //   return map;
  // }
  public NoticeUriResponseVO noticeUri(Long memberNo, Pageable pageable){
    // Page<NoticeImgInfoEntity> page = noticeImgInfoRepository.findByNiiSeq(noticeNo,pageable);
    Page<NoticeImgInfoEntity> page = noticeImgInfoRepository.findByNiiSeq(memberNo, pageable);
    // Page<NoticeImgInfoEntity> page = noticeImgInfoRepository.findByNiiMbiSeq(memberNo,pageable);
    NoticeUriResponseVO response = NoticeUriResponseVO.builder()
    .List(page.getContent())
    .msg("조회되었습니다.")
    .code(200)
    .build();
    return response;
  }

  public NoticeResponseVO noticeList(Long memberNo){
    MemberBasicInfoEntity member = memberBasicInfoRepository.findByMbiSeq(memberNo);
    // ShareAccountInfoEntity share = shareAccountInfoRepository.findById(saiSeq).orElse(null);
    if(member == null){
      NoticeResponseVO response = NoticeResponseVO.builder()
      .msg("해당 개시글이 없습니다.")
      .code(404)
      .build();
      return response;
    }
    // Map<String,Object> map =new LinkedHashMap<String,Object>();
    // map.put("list", noticeInfoRepository.findAllByNiSeq(data.getNiSeq()));
    // map.put("code", HttpStatus.OK);
    List<NoticeInfoEntity> page = noticeInfoRepository.findByMemberInfo(member);
    NoticeResponseVO response = NoticeResponseVO.builder()
    .List(page)
    .msg("공지사항이 조회되었습니다.")
    .code(200)
    .build();
    return response;
  }
  // public ResponseVO noticeDetail(Long memberNo, Pageable pageable){
  //   // MemberBasicInfoEntity member = memberBasicInfoRepository.findByMbiSeq(memberNo);
  //   Page<NoticeImgInfoEntity> page = noticeImgInfoRepository.findByNiiMbiSeq(memberNo, pageable);
  //     ResponseVO response = ResponseVO.builder()
  //     .List(page.getContent())
  //     // .total(page.getTotalElements())
  //     .code(200)
  //     .msg("리스트가 조회되었습니다.")
  //     .build();
  //   return response;
  //   }
  
    public ResponseVO noticeDetail(Long noticeNo){
      // MemberBasicInfoEntity member = memberBasicInfoRepository.findByMbiSeq(memberNo);
      // NoticeImgInfoEntity data = noticeImgInfoRepository.findByNiiMbiSeq(memberNo);
      NoticeInfoEntity data = noticeInfoRepository.findByNiSeq(noticeNo);
      NoticeImgInfoEntity data1 = noticeImgInfoRepository.findByNoticeInfo(data);
        ResponseVO response = ResponseVO.builder()
        .noticeNo(data.getNiSeq())
        .memo(data.getNiMemo())
        .date(data.getNiDate())
        .uri(data1.getNiiFileName())
        .code(200)
        .msg("리스트가 조회되었습니다.")
        .build();
      return response;
      }



public /*Map<String,Object>*/NoticeUploadVO addNotice(NoticeVO data, MultipartFile file, Long memberNo){
  Map<String,Object> map = new LinkedHashMap<String,Object>();
  MemberBasicInfoEntity member = memberBasicInfoRepository.findByMbiSeq(memberNo);
  if(data == null){
    ResponseVO vo = ResponseVO.builder()
    .code(400)
    .msg("사진 또는 공지사항을 입력해주세요")
    .build();
    // map.put("status", false);
    // map.put("msg", "사진 또는 공지사항을 입력해주세요");
    // map.put("code",HttpStatus.BAD_REQUEST);  
  }
  NoticeInfoEntity noticeInfo = NoticeInfoEntity.builder()
  .niMemo(data.getMemo()).memberInfo(member).build();
  System.out.println(noticeInfo);
  noticeInfo = noticeInfoRepository.save(noticeInfo);
  map = addNoticeImage(file,noticeInfo,memberNo);
  return NoticeUploadVO.builder().code(200).msg("등록완료").build();
}
public Map<String,Object> addNoticeImage(MultipartFile file, NoticeInfoEntity noticeInfo, Long memberNo){
  Map<String,Object> map = new LinkedHashMap<String,Object>();
  if(file != null){
    addFileImage(file, noticeInfo, memberNo);
    map.put("status",true);
    map.put("msg", "공지사항이 저장되었습니다.");
    map.put("code", HttpStatus.CREATED);
    }
  else{
    map.put("status", false);
    map.put("msg", "업로드에 실패했습니다.");
    map.put("code", HttpStatus.BAD_REQUEST);
    }
    return map;
  }
  
  @Value("${file.image.notice}") 
  String notice_img;
  public void addFileImage(MultipartFile file, NoticeInfoEntity noticeInfo,Long memberNo){
    // Path folderLocation = Paths.get(notice_img);
    // String originName = file.getOriginalFilename();
    // String noticeFileName = createNoticeFileName(originName);
    // Path targetFile = folderLocation.resolve(noticeFileName);
    // try{
    //   Files.copy(file.getInputStream(),targetFile, StandardCopyOption.REPLACE_EXISTING);
    // }
    // catch(Exception e){
    //   e.printStackTrace();
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

    NoticeImgInfoEntity imgfile = NoticeImgInfoEntity.builder()
    .niiFileName(fileName)
    .niiUri("http://localhost:9090/api/img/download"+fileName)
    .noticeInfo(noticeInfo)
    .niiMbiSeq(memberNo)
    .build();
    noticeImgInfoRepository.save(imgfile);
      }


      private String createNoticeFileName(String originalFilename){
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid+"."+ext;
      }
      private String extractExt(String originalFilename){
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
      }
      public String getIiFileNameByUri(String niiUri){
        NoticeImgInfoEntity data = noticeImgInfoRepository.findByNiiUri(niiUri);
        return data.getNiiFileName();
      
      }

      public NoticeUploadVO deleteNotice(Long noticeNo){
          NoticeUploadVO response = NoticeUploadVO.builder()
          .msg("공지사항이 삭제되었습니다.")
          .code(200)
          .build();
          return response;
        }

        public /*Map<String,Object>*/NoticeUploadVO updateNotice(NoticeVO data, MultipartFile file, Long noticeNo ,Long memberNo ){
          Map<String,Object> map = new LinkedHashMap<String,Object>();
          MemberBasicInfoEntity member = memberBasicInfoRepository.findByMbiSeq(memberNo);
          if(data == null){
            ResponseVO vo = ResponseVO.builder()
            .code(400)
            .msg("사진 또는 공지사항을 입력해주세요")
            .build();
            // map.put("status", false);
            // map.put("msg", "사진 또는 공지사항을 입력해주세요");
            // map.put("code",HttpStatus.BAD_REQUEST);
          }
          noticeInfoRepository.deleteByNiSeq(noticeNo); 
          NoticeInfoEntity noticeInfo = NoticeInfoEntity.builder()
          .niMemo(data.getMemo()).memberInfo(member).build();
          System.out.println(noticeInfo);
          noticeInfo = noticeInfoRepository.save(noticeInfo);
          map = updateNoticeImage(file,noticeInfo, noticeNo,memberNo);
          return NoticeUploadVO.builder().code(200).msg("수정완료").build();
        }



        public Map<String,Object> updateNoticeImage(MultipartFile file, NoticeInfoEntity noticeInfo, Long noticeNo ,Long memberNo){
          Map<String,Object> map = new LinkedHashMap<String,Object>();
          if(file != null){ 
            
            updateFileImage(file, noticeInfo,memberNo);
            map.put("status",true);
            map.put("msg", "공지사항이 수정되었습니다.");
            map.put("code", HttpStatus.CREATED);
            }
          else{
            map.put("status", false);
            map.put("msg", "업로드에 실패했습니다.");
            map.put("code", HttpStatus.BAD_REQUEST);
            }

            return map;
          }
          
          @Value("${file.image.notice}") 
          String notice_image;
          public void updateFileImage(MultipartFile file, NoticeInfoEntity noticeInfo,Long memberNo){
            // Path folderLocation = Paths.get(notice_image);
            // String originName = file.getOriginalFilename();
            // String noticeFileName = createNoticeFileName(originName);
            // Path targetFile = folderLocation.resolve(noticeFileName);
            // try{
            //   Files.copy(file.getInputStream(),targetFile, StandardCopyOption.REPLACE_EXISTING);
            // }
            // catch(Exception e){
            //   e.printStackTrace();
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

            NoticeImgInfoEntity imgfile = NoticeImgInfoEntity.builder()
            .niiFileName(fileName)
            .niiUri("http://localhost:9090/api/img/download"+fileName)
            .noticeInfo(noticeInfo)
            .niiMbiSeq(memberNo)
            .build();
            noticeImgInfoRepository.save(imgfile);
          }
      }


