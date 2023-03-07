package com.secondproject.coupleaccount.api.accountbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.secondproject.coupleaccount.service.AddAccountBookInfoService;
import com.secondproject.coupleaccount.vo.AddExpenseInfoVO;
import com.secondproject.coupleaccount.vo.AddImportInfoVO;
import com.secondproject.coupleaccount.vo.UpdateExpenseInfoVO;
import com.secondproject.coupleaccount.vo.UpdateImportInfoVO;
import com.secondproject.coupleaccount.vo.response.ExpenseInfoResponseVO;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "가계부 조회를 제외한 모든 기능", description = "가계부 입금 및 지출 정보 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accountbook")
public class AddAccountBookAPIController {
    private final AddAccountBookInfoService AddAccountBookInfoService;
  
    // 입금 추가 
    @Operation(summary = "수입 정보 추가", description="수입 정보를 추가합니다.")
    @PostMapping("/import/add")
    public ResponseEntity<ExpenseInfoResponseVO> AddImportInfo(Authentication authentication, @RequestBody AddImportInfoVO data) {
        // Map<String, Object> resultMap = AddAccountBookInfoService.addImportInfo(mbiSeq, data);
         Long mbiSeq = Long.parseLong(authentication.getName());      
        return new ResponseEntity<ExpenseInfoResponseVO>(AddAccountBookInfoService.addImportInfo(mbiSeq, data), HttpStatus.CREATED);
    }
    // 입금 수정
    @Operation(summary = "수입 정보 수정", description="수입 정보를 수정합니다.")
    @PostMapping("/import/update")
    public ResponseEntity<ExpenseInfoResponseVO> AddImportInfo(@Parameter(description = "수정할 입금 정보 번호", example = "1") @RequestParam Long iiSeq,Authentication authentication,
            @RequestBody UpdateImportInfoVO data) {
            Long mbiSeq = Long.parseLong(authentication.getName());
        return new ResponseEntity<ExpenseInfoResponseVO>(AddAccountBookInfoService.updateImportInfo(iiSeq, mbiSeq, data), HttpStatus.ACCEPTED);
    }

    // 지출 추가 
    @PostMapping("/expense/add")
    @Operation(summary = "지출 정보 추가", description="지출 정보를 추가합니다.")
    public ResponseEntity<ExpenseInfoResponseVO> AddExpenseInfo  (@RequestPart(value = "file") @Nullable MultipartFile file, Authentication authentication,
            @RequestPart(value = "json") AddExpenseInfoVO data) throws Exception
            
    {
            Long mbiSeq = Long.parseLong(authentication.getName());
        return new ResponseEntity<ExpenseInfoResponseVO>(AddAccountBookInfoService.addExpenseInfo(file, mbiSeq, data), HttpStatus.CREATED);
    }

    // 지출 수정
    @PostMapping("/expense/update")
    @Operation(summary = "지출 정보 수정", description="지출 정보를 수정합니다.")
    public ResponseEntity<ExpenseInfoResponseVO> UpdateExpenseInfo (Authentication authentication, Long eiSeq, @RequestPart(value = "file") @Nullable MultipartFile file,
            @RequestPart(value = "json") UpdateExpenseInfoVO data) throws Exception {
             Long mbiSeq = Long.parseLong(authentication.getName());
                return new ResponseEntity<ExpenseInfoResponseVO>(
            
                AddAccountBookInfoService.updateExpenseInfo(file, mbiSeq, data, eiSeq), HttpStatus.ACCEPTED);
    }

    // 지출 이미지 삭제 
    @DeleteMapping("/expense/img/delete")
    @Operation(summary = "지출 정보 이미지 삭제", description = "지출 정보 이미지를 삭제합니다.")
    public ResponseEntity<ExpenseInfoResponseVO> DeleteExpenseImg(@RequestParam Long eiSeq) {
        return new ResponseEntity<ExpenseInfoResponseVO>(
                AddAccountBookInfoService.deleteExpenseImg(eiSeq), HttpStatus.ACCEPTED);
    }
    
    // 입금 정보 삭제
    @DeleteMapping("/import/delete")
    @Operation(summary = "입금 정보 삭제", description = "입금 정보를 삭제합니다.")
    public ResponseEntity<ExpenseInfoResponseVO> DeleteImportInfo(@RequestParam Long iiSeq) {
        return new ResponseEntity<ExpenseInfoResponseVO>(
                AddAccountBookInfoService.deleteImportInfo(iiSeq), HttpStatus.ACCEPTED);
    }

    // 지출 정보 삭제
    @DeleteMapping("/expense/delete")
    @Operation(summary = "지출 정보 삭제", description = "지출 정보를 삭제합니다.")
    public ResponseEntity<ExpenseInfoResponseVO> DeleteExpenseInfo(@RequestParam Long eiSeq) {
        return new ResponseEntity<ExpenseInfoResponseVO>(
                AddAccountBookInfoService.deleteExpenseInfo(eiSeq), HttpStatus.ACCEPTED);
    }
}
   

