package com.secondproject.coupleaccount.api.accountbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.service.AccountBookInfoListService;
import com.secondproject.coupleaccount.vo.response.AccountBookListDayResponseVO;
import com.secondproject.coupleaccount.vo.response.AccountBookListResponseVO;
import com.secondproject.coupleaccount.vo.response.AccountBookResponseVO;
import com.secondproject.coupleaccount.vo.response.AccountImportByPersonResponseVO;
import com.secondproject.coupleaccount.vo.response.ExpenseDetailResponseVO;
import com.secondproject.coupleaccount.vo.response.ImportDetailResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
@Tag(name = "가계부 조회", description = "가계부 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accountbook/list")
public class AccountBookListAPIController {
    private final AccountBookInfoListService accountBookInfoListService;
    private final MemberBasicInfoRepository mRepo;
    // 공유통장번호에 따른 총 지출수입리스트
    @Operation(summary = "공유 지출수입", description="총 지출수입정보를 조회합니다.")
    @GetMapping("/totalprice")
    public ResponseEntity<AccountBookResponseVO> getTotalExpenseImportList(Authentication authentication) {
            Long mbiSeq = Long.parseLong(authentication.getName());
           MemberBasicInfoEntity entity = mRepo.findById(mbiSeq).get();
        return new ResponseEntity<AccountBookResponseVO>(accountBookInfoListService.TotalList(entity.getShareAccount().getSaiSeq()),
                HttpStatus.ACCEPTED);
    }
    @Operation(summary = "월 공유 지출수입조회", description="한달치의 공유 지출수입을 조회합니다.")
    @GetMapping("/month/couple")
    public ResponseEntity<AccountBookListResponseVO> getMonthAccountBookList(Authentication authentication,
           @Parameter(description = "조회할 년도", example = "2023")@RequestParam Integer year, @Parameter(description = "조회할 월 ", example = "2")@RequestParam Integer month) {
                Long mbiSeq = Long.parseLong(authentication.getName());
           MemberBasicInfoEntity entity = mRepo.findById(mbiSeq).get();
        return new ResponseEntity<AccountBookListResponseVO>(
                accountBookInfoListService.getMonthAccountBookList(entity.getShareAccount().getSaiSeq(), year, month),
                HttpStatus.ACCEPTED);
    }
    
    @Operation(summary = "일 공유 지출수입조회", description="조회할 일의 공유 지출수입을 조회합니다.")
    @GetMapping("/day/couple")
    public ResponseEntity<AccountBookListDayResponseVO> getDayAccountBookList(
            @Parameter(description = "공유통장번호", example = "1") Authentication authentication, @RequestParam Integer year,
                 @Parameter(description = "조회할 월", example = "3") @RequestParam Integer month,
            @Parameter(description = "조회할 일", example = "12") @RequestParam Integer day) {
            Long mbiSeq = Long.parseLong(authentication.getName());
            MemberBasicInfoEntity entity = mRepo.findById(mbiSeq).get();
        return new ResponseEntity<AccountBookListDayResponseVO>(
                accountBookInfoListService.getDayAccountBookList(entity.getShareAccount().getSaiSeq(), year, month, day),
                HttpStatus.ACCEPTED);
    }

    @Operation(summary = "지출번호에 따른 상세정보 조회", description="해당 지출정보의 상세정보를 조회합니다.")
    @GetMapping("/expense/detail")
    public ResponseEntity<ExpenseDetailResponseVO> getExpenseDetail(
            @Parameter(description = "지출 고유번호", example = "1") @RequestParam Long eiSeq) {
        return new ResponseEntity<ExpenseDetailResponseVO>(
                accountBookInfoListService.getExpenseDetail(eiSeq), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "입금번호에 따른 상세정보 조회", description="해당 입금정보의 상세정보를 조회합니다.")
    @GetMapping("/import/detail")
    public ResponseEntity<ImportDetailResponseVO> getImportDetail(
                    @Parameter(description = "입금 고유번호", example = "1") @RequestParam Long iiSeq) {
            return new ResponseEntity<ImportDetailResponseVO>(
                            accountBookInfoListService.getImportDetail(iiSeq), HttpStatus.ACCEPTED);
    }
    
    @Operation(summary = "본인의 월 총지출입금액 조회", description = "본인이 월 총지출입금액을 조회합니다.")
    @GetMapping("/month/person")
    public ResponseEntity<AccountImportByPersonResponseVO> getExpenseImportByPersonList(
            Authentication authentication,
            @Parameter(description = "조회할 년도", example = "2023") @RequestParam Integer year,
                    @Parameter(description = "조회할 월 ", example = "2") @RequestParam Integer month) {
                   Long mbiSeq = Long.parseLong(authentication.getName());
            return new ResponseEntity<AccountImportByPersonResponseVO>(
                            accountBookInfoListService.getExpenseImportByPersonTotalList(mbiSeq, year, month),
                            HttpStatus.ACCEPTED);
    }
@Operation(summary = "본인의 일 지출수입조회", description="조회할 본인의 일 지출수입액을 조회합니다.")
    @GetMapping("/day/person")
    public ResponseEntity<AccountBookListDayResponseVO> getDayAccountBookByPersonList(
           Authentication authentication, @Parameter(description = "조회할 년도", example = "2023") @RequestParam Integer year,
                 @Parameter(description = "조회할 월", example = "3") @RequestParam Integer month,
                    @Parameter(description = "조회할 일", example = "12") @RequestParam Integer day) {
            Long mbiSeq = Long.parseLong(authentication.getName());
            return new ResponseEntity<AccountBookListDayResponseVO>(
                            accountBookInfoListService.getDayAccountBookByPersonList(mbiSeq, year, month, day),
                            HttpStatus.ACCEPTED);
    }
    @Operation(summary = "상대방의 월 총지출입금액 조회", description ="상대방의 월 총 지출입금금액을 조회합니다.")
    @GetMapping("/month/otherperson")
    public ResponseEntity<AccountImportByPersonResponseVO> getExpenseImportByOtherPersonList(Authentication authentication, @Parameter(description = "조회할 년도", example = "2023") @RequestParam Integer year,
                 @Parameter(description = "조회할 월", example = "3") @RequestParam Integer month
    ) {
            Long mbiSeq = Long.parseLong(authentication.getName());
            return new ResponseEntity<AccountImportByPersonResponseVO>(
                            accountBookInfoListService.getExpenseImportByOtherPersonTotalList(mbiSeq, year, month),
                            HttpStatus.ACCEPTED);
    }
    @Operation(summary = "상대방의 일 총 지출수입조회", description="조회할 상대방의 일 총 지출수입액을 조회합니다.")
    @GetMapping("/day/otherperson")
    public ResponseEntity<AccountBookListDayResponseVO> getDayAccountBookByOtherPersonList(
           Authentication authentication, @Parameter(description = "조회할 년도", example = "2023") @RequestParam Integer year,
                 @Parameter(description = "조회할 월", example = "3") @RequestParam Integer month,
                    @Parameter(description = "조회할 일", example = "12") @RequestParam Integer day) {
            Long mbiSeq = Long.parseLong(authentication.getName());
            return new ResponseEntity<AccountBookListDayResponseVO>(
                            accountBookInfoListService.getDayAccountBookByOtherPersonList(mbiSeq, year, month, day),
                            HttpStatus.ACCEPTED);
    }
}