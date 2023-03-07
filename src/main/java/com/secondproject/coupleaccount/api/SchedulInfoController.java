package com.secondproject.coupleaccount.api;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.service.SchedulInfoService;
import com.secondproject.coupleaccount.vo.schdule.SchedulInfoVO;
import com.secondproject.coupleaccount.vo.schdule.SchedulallVO;
import com.secondproject.coupleaccount.vo.schdule.ScheduleImgInfoVo;
import com.secondproject.coupleaccount.vo.schdule.ScheduleResponseVO;
import com.secondproject.coupleaccount.vo.schdule.ScheduleResponseVO1;
import com.secondproject.coupleaccount.vo.schdule.ScheduleUpdateVO;
import com.secondproject.coupleaccount.vo.schdule.schedualDetailResponseVO;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;



@Tag(name = "캘린더 관련 api" ,description="캘린더 관련CRUD API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SchedulInfoController {
    // @Autowired SchedulInfoService schedulInfoService;
    private final SchedulInfoService schedulInfoService;
    private final MemberBasicInfoRepository memberBasicInfoRepository;
    
    // 스케줄 추가
    @PutMapping("/calendar/put")
    @Operation(summary = "캘린더 추가", description="캘린더를 내용을 추가 합니다..")
    public ResponseEntity<ScheduleImgInfoVo> Schedulimg(@RequestPart(value = "file") @Nullable MultipartFile file, 
    // Long mbiSeq,
    Authentication authentication,
            @RequestPart(value = "json") SchedulInfoVO data) throws Exception
            {
        Long mbiSeq = Long.parseLong(authentication.getName());
        return new ResponseEntity<ScheduleImgInfoVo>(schedulInfoService.addschedulInfo(file, mbiSeq, data), HttpStatus.CREATED);
    }


    // @GetMapping("/calendar/list")
    // @Operation(summary="캘린더 날짜별 리스트", description = "등록된 캘린더를 보여줍니다.") 
    // public ResponseEntity<SchedulallVO>calendarlist(@PageableDefault(sort = "siStartDate", direction =  Direction.ASC)Pageable pageable, SchedulallVO data){
    //     return new ResponseEntity<SchedulallVO>(schedulInfoService.AllList(data), HttpStatus.ACCEPTED);
    // } 


    // 스케줄 삭제
    @Operation(summary="캘린더 삭제", description = "등록된 캘린더를 삭제합니다.") 
    @DeleteMapping("/calendar/delete")
    public ResponseEntity<Object>calendarDelete(@RequestParam Long siSeq) {
        Map<String, Object> map = new LinkedHashMap<String ,Object>();
        schedulInfoService.scheduledelete(siSeq);
        map.put("message", "캘린더 내용을 삭제했습니다");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

   // 스케줄 수정
    @PostMapping("/calendar/update")
    @Operation(summary = "스케줄 정보 수정", description="스케줄 정보를 수정합니다.")
    public ResponseEntity<ScheduleImgInfoVo> updateSchedule( @RequestPart(value = "file") @Nullable MultipartFile file, 
    // Long mbiSeq, 
    Authentication authentication,
    Long siSeq,
        @RequestPart(value = "json") ScheduleUpdateVO data) {
            Long mbiSeq = Long.parseLong(authentication.getName());  
        return new ResponseEntity<ScheduleImgInfoVo>(schedulInfoService.updateSchedue(file, mbiSeq, data, siSeq), HttpStatus.ACCEPTED);
}

    // 스케줄 이미지 삭제
@DeleteMapping("/calendar/img/delete")
    @Operation(summary = "스케줄 이미지 삭제", description = "스케줄 이미지를 삭제합니다.")
    public ResponseEntity<ScheduleImgInfoVo> deleteSchedule(@RequestParam Long siSeq) {
        return new ResponseEntity<ScheduleImgInfoVo>(
            schedulInfoService.deleteSchedule(siSeq), HttpStatus.ACCEPTED);
    }

    // 디테일 리스트 조회
    // @GetMapping("/schedule/couple/detail")
    // @Operation(summary = "디테일 리스트", description = "디테일 리스트를 보여줍니다.")
    // public ResponseEntity<ScheduleResponseVO> getScheduleMonthCouple(
    //     // @RequestParam Long saiSeq, 
    //     @RequestParam Integer year,@RequestParam Integer month
    // ,  Authentication authentication){
        
    //     Long mbiSeq = Long.parseLong(authentication.getName());
    //     MemberBasicInfoEntity memberBasicInfoEntity = memberBasicInfoRepository.findByMbiSeq(mbiSeq);
    //     Long saiSeq = memberBasicInfoEntity.getShareAccount().getSaiSeq();

    // return new ResponseEntity<>(schedulInfoService.getCoupleSchedule(saiSeq, year, month), HttpStatus.ACCEPTED);
    // }


     // 월별 리스트 조회
    @GetMapping("/schedule/couple/month")
    @Operation(summary = "월별 리스트", description = "월별 리스트를 보여줍니다.")
    public ResponseEntity<ScheduleResponseVO1> getScheduleMonthCouple1(
        // @RequestParam Long saiSeq, 
        @RequestParam Integer year,@RequestParam Integer month,
        Authentication authentication
        ){
            Long mbiSeq = Long.parseLong(authentication.getName());
            MemberBasicInfoEntity memberBasicInfoEntity = memberBasicInfoRepository.findByMbiSeq(mbiSeq);
            Long saiSeq = memberBasicInfoEntity.getShareAccount().getSaiSeq();

    return new ResponseEntity<>(schedulInfoService.getCoupleSchedule1(saiSeq, year, month), HttpStatus.ACCEPTED);
    }    



    @Operation(summary = "디테일 리스트", description = "디테일 리스트를 보여줍니다.")
    @GetMapping("/schedule/couple/detail")
    public ResponseEntity<schedualDetailResponseVO> asdfasdf(@RequestParam Long saiSeq       
    ) {
        return new ResponseEntity<>(schedulInfoService.getDeatailSchedule(saiSeq), HttpStatus.OK);

    }
}

    

