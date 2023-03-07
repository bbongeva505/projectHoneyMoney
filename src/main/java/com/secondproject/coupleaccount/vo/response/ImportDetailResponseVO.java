package com.secondproject.coupleaccount.vo.response;

import com.secondproject.coupleaccount.vo.ImportDetailVO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImportDetailResponseVO {
    private Boolean status;
    private String message;
    private ImportDetailVO importDetail;
}
