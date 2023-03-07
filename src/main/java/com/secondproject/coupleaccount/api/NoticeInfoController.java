package com.secondproject.coupleaccount.api;

import java.util.LinkedHashMap;

import java.util.Map;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.secondproject.coupleaccount.repository.NoticeImgInfoRepository;
import com.secondproject.coupleaccount.repository.NoticeInfoRepository;

import com.secondproject.coupleaccount.service.NoticeInfoService;

import com.secondproject.coupleaccount.vo.NoticeVO;
import com.secondproject.coupleaccount.vo.notice.NoticeResponseVO;
import com.secondproject.coupleaccount.vo.notice.NoticeUploadVO;
import com.secondproject.coupleaccount.vo.notice.NoticeUriResponseVO;
import com.secondproject.coupleaccount.vo.notice.ResponseVO;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.transaction.Transactional;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@Tag(name="공지사항 관리", description = "공지사항 CRUD API")
@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor

public class NoticeInfoController {
  
  private final NoticeInfoRepository noticeInfoRepository;
  private final NoticeImgInfoRepository noticeImgInfoRepository;
  private final NoticeInfoService noticeInfoService;
  @Operation(summary = "공지사항 리스트", description="등록된 공지사항를 보여줍니다.")
  @GetMapping("list")
  public ResponseEntity<NoticeResponseVO> noticeList(@RequestParam Long memberNo){
    return new ResponseEntity<NoticeResponseVO>(noticeInfoService.noticeList(memberNo),HttpStatus.OK);
  }
    @Operation(summary = "공지사항 uri", description="등록된 uri을 10개 단위로 보여줍니다.")
    @GetMapping("uri")
    public ResponseEntity<NoticeUriResponseVO> getUriList(
      Authentication authentication,
    // @Parameter(description="회원 번호",example="1")  
    // @RequestParam Long memberNo, 
    @Parameter(hidden = true) @PageableDefault(size=10, sort = "niiSeq",direction = Sort.Direction.DESC)Pageable pageable){
      Long memberNo = Long.parseLong(authentication.getName());
      return new ResponseEntity<NoticeUriResponseVO>(noticeInfoService.noticeUri(memberNo, pageable),HttpStatus.OK);
    }
    
    // @Operation(summary = "공지사항 상세보기", description="등록된 공지사항을 10개 단위로 보여줍니다.")
    // @GetMapping("/detail")
    // public /*Map<String,Object>*/ResponseEntity<ResponseVO> getNoticeDetailList(
    //   @Parameter(hidden = true) @PageableDefault(size=10, sort = "niiMbiSeq",direction = Sort.Direction.DESC)
    //   Pageable pageable,
    //  @Parameter(description="회원번호",example="1") @RequestParam Long memberNo){
    //   // Map<String,Object> map = new LinkedHashMap<String,Object>();
    //   // map.put("list", noticeListRepository.findByMbiSeq(memberNo));
      
    //   return new ResponseEntity<ResponseVO>(noticeInfoService.noticeDetail(memberNo, pageable),HttpStatus.OK);
    // }

    @Operation(summary = "공지사항 상세보기", description="등록된 공지사항을 10개 단위로 보여줍니다.")
    @GetMapping("/detail")
    public /*Map<String,Object>*/ResponseEntity<ResponseVO> getNoticeDetailList(
      // @Parameter(hidden = true) @PageableDefault(size=10, sort = "niiMbiSeq",direction = Sort.Direction.DESC)
      // Pageable pageable,
     @Parameter(description="공지사항 번호",example="1")   @RequestParam Long noticeNo){
      // Map<String,Object> map = new LinkedHashMap<String,Object>();
      // map.put("list", noticeListRepository.findByMbiSeq(memberNo));
      return new ResponseEntity<ResponseVO>(noticeInfoService.noticeDetail(noticeNo),HttpStatus.OK);
    }

    @Operation(summary = "공지사항 및 이미지 추가", description="공지사항이 추가되었습니다.")
    @PutMapping("/add")
    public /*Map<String,Object>*/ ResponseEntity<NoticeUploadVO> addNotioce( 
    @RequestPart @Nullable  @Parameter(description="formdata로 데이터를 입력합니다.(memo : 공지사항내용, MbiSeq : 회원번호,")NoticeVO noticeVO,
    @RequestPart @Nullable  @Parameter(description = "formdata로 데이터를 입력합니다.") MultipartFile file,
    // @RequestParam Long memberNo
    Authentication authentication
    ){
    // Map<String,Object> map = new LinkedHashMap<String,Object>();
    //  map = noticeInfoService.addNotice(noticeVO, file);
    Long memberNo = Long.parseLong(authentication.getName());
    return new ResponseEntity<NoticeUploadVO>(noticeInfoService.addNotice(noticeVO, file ,memberNo),HttpStatus.OK);
  }

  @Operation(summary =  "공지사항 및 이미지 수정", description="공지사항이 수정되었습니다.")
  @PostMapping("/update")
  @Transactional
  public ResponseEntity<NoticeUploadVO> updateNotice(
    @RequestPart @Nullable  @Parameter(description="formdata로 데이터를 입력합니다.(memo : 공지사항내용, MbiSeq : 회원번호") NoticeVO noticeVO,
    @RequestPart @Nullable  @Parameter(description = "formdata로 데이터를 입력합니다.") MultipartFile file,
    @RequestParam  @Parameter(description="formdata로 데이터를 입력합니다.(noticeNo : 수정할 공지사항 번호") Long noticeNo,
    // @RequestParam Long memberNo
    Authentication authentication
  ){
    noticeImgInfoRepository.deleteByNiiSeq(noticeNo);
    Long memberNo = Long.parseLong(authentication.getName());

    return new ResponseEntity<NoticeUploadVO>(noticeInfoService.updateNotice(noticeVO, file, noticeNo,memberNo),HttpStatus.CREATED);
  }


  @Operation(summary = "공지사항 삭제", description="삭제하였습니다.")
  @DeleteMapping("/delete")
  @Transactional
  public ResponseEntity<NoticeUploadVO> deleteNotice(  
  @RequestParam Long noticeNo){
    noticeInfoRepository.deleteByNiSeq(noticeNo); 
    noticeImgInfoRepository.deleteByNiiSeq(noticeNo); 
    return new ResponseEntity<NoticeUploadVO>(noticeInfoService.deleteNotice(noticeNo),HttpStatus.OK);
  }

  // @PatchMapping("/update")
  // public ResponseEntity<Object>updateNotice(
  //   @RequestParam Long mbiSeq, @RequestParam Long noticeNo,  @RequestParam String value
  // ){
  //   Map<String,Object> map = new LinkedHashMap<String,Object>();
  //   NoticeInfoEntity entity = noticeInfoRepository.findById(noticeNo).get();
  //   if(noticeInfoRepository.conutByNiSeq(noticeNo) != 1){
  //     map.put("status", false);
  //     map.put("msg","공지사항 번호를 다시 한번 확인헤주세요");
  //     return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
  //   }
    
  //   return new ResponseEntity<>(map,HttpStatus.OK);
  //   }


}
