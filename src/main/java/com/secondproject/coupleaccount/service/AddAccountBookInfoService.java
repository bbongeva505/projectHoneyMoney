package com.secondproject.coupleaccount.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.secondproject.coupleaccount.entity.AccountBookImgEntity;
import com.secondproject.coupleaccount.entity.ExpenseInfoEntity;
import com.secondproject.coupleaccount.entity.ImportInfoEntity;
import com.secondproject.coupleaccount.repository.AccountBookImgRepository;
import com.secondproject.coupleaccount.repository.CategoryInfoRepository;
import com.secondproject.coupleaccount.repository.ExpenseInfoRepository;
import com.secondproject.coupleaccount.repository.ImportInfoRepository;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.utils.CommonUtils;
import com.secondproject.coupleaccount.vo.AddExpenseInfoVO;
import com.secondproject.coupleaccount.vo.AddImportInfoVO;
import com.secondproject.coupleaccount.vo.UpdateExpenseInfoVO;
import com.secondproject.coupleaccount.vo.UpdateImportInfoVO;
import com.secondproject.coupleaccount.vo.response.ExpenseInfoResponseVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddAccountBookInfoService {
    @Value("${file.image.accountbook}")
    String accountbook_img;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    
    private final MemberBasicInfoRepository mRepo;
    private final ImportInfoRepository iRepo;
    private final CategoryInfoRepository cateRepo;
    private final ExpenseInfoRepository eRepo;
    private final AccountBookImgService accountBookImgService;
    private final AccountBookImgRepository accounImgRepo;
    private final AmazonS3Client amazonS3Client;



    // ?????? ??????
    public ExpenseInfoResponseVO addImportInfo(Long miSeq, AddImportInfoVO data) {
        ImportInfoEntity entity = new ImportInfoEntity();
        if (mRepo.countByMbiSeq(miSeq) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("????????? ?????????????????????.")
                    .build();
            return result;
        }

        else if (data.getPrice() < 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ??????????????????.")
                    .build();
            return result;
        }
        entity.setIiPrice(data.getPrice());
        entity.setIiMemo(data.getMemo());
        entity.setMemberBasicInfoEntity(mRepo.findById(miSeq).get());
        entity.setIiStatus(data.getImportStatus());
        entity.setIiDate(data.getImportDate());
        iRepo.save(entity);
        ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(true).message("?????? ????????? ?????????????????????.").build();
        return result;
    }

    // ?????? ??????
    public ExpenseInfoResponseVO updateImportInfo(Long iiSeq, Long miSeq, UpdateImportInfoVO data) {
        if (iRepo.countByIiSeqAndIiMbiSeq(iiSeq, miSeq) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false)
                    .message("??????????????? ????????? ????????? ?????? ???????????????????????????.").build();
            return result;
        }
        ImportInfoEntity entity = iRepo.findById(iiSeq).get();
        if (data.getUpdatePrice() == null) {
            data.setUpdatePrice(entity.getIiPrice());
        } else if (data.getUpdatePrice() < 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ??????????????????.")
                    .build();
            return result;
        }
        if (data.getUpdateMemo() == null) {
            data.setUpdateMemo(entity.getIiMemo());
        }
        if (data.getUpdateMemo() == null) {
            data.setUpdateMemo(entity.getIiMemo());
        }
        if (data.getUpdateDate() == null) {
            data.setUpdateDate(entity.getIiDate());
        }
        if (data.getUpdateStatus() == null) {
            data.setUpdateStatus(entity.getIiStatus());
        }
        entity.setIiPrice(data.getUpdatePrice());
        entity.setIiMemo(data.getUpdateMemo());
        entity.setIiDate(data.getUpdateDate());
        entity.setIiStatus(data.getUpdateStatus());
        iRepo.save(entity);
        ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(true).message("?????? ????????? ?????????????????????.").build();
        return result;
    }


    // ?????? ??????
    public ExpenseInfoResponseVO addExpenseInfo(@RequestPart(value = "file") MultipartFile file, Long mbiSeq,
            @RequestPart(value = "json") AddExpenseInfoVO data) throws Exception {
        if (mRepo.countByMbiSeq(mbiSeq) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ?????????????????????.")
                    .build();
            return result;
        }

        else if (data.getPrice() < 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ??????????????????.")
                    .build();
            return result;
        } else if (cateRepo.countByCateSeq(data.getCateSeq()) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ???????????????????????????.")
                    .build();
            return result;
        }

        // Path folderLocation = Paths.get(accountbook_img);

        AccountBookImgEntity data1 = null;
        
        if (file != null) {
            // String originFileName = file.getOriginalFilename();
            // if (!originFileName.equals("")) {
            //     String[] split = originFileName.split("\\.");
            //     String ext = split[split.length - 1];
            //     String filename = "";
            //     for (int i = 0; i < split.length - 1; i++) {
            //         filename += split[i];
            //     }
            //     String saveFileName = "account" + "-";
            //     Calendar c = Calendar.getInstance();
            //     saveFileName += c.getTimeInMillis() + "." + ext;
            //     Path targetFile = folderLocation.resolve(saveFileName);
            //     try {
            //         Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            //     } catch (Exception e) {
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
                data1 = new AccountBookImgEntity();
                data1.setAiImgName(fileName);
                data1.setAiUri(fileName);
                accountBookImgService.addAccountBookImgInfo(data1);
            }        

        ExpenseInfoEntity entity = new ExpenseInfoEntity(); 
        
        entity.setEiPrice(data.getPrice());
        entity.setEiDate(data.getDate());
        entity.setCategoryInfoEntity(cateRepo.findById(data.getCateSeq()).get());
        entity.setEiStatus(data.getStatus());
        entity.setEiMemo(data.getMemo());
        entity.setMemberBasicInfoEntity(mRepo.findById(mbiSeq).get());
        entity.setAccountBookImgEntity(data1);
        eRepo.save(entity);
        ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(true).message("??????????????? ?????????????????????.").build();
        return result;
    }

    // ?????? ??????
    public ExpenseInfoResponseVO updateExpenseInfo(@RequestPart(value = "file") MultipartFile file, Long mbiSeq,
            @RequestPart(value = "json") UpdateExpenseInfoVO data, Long eiSeq) throws Exception {

        if (eRepo.countByEiSeqAndEiMbiSeq(eiSeq, mbiSeq) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false)
                    .message("??????????????? ????????? ????????? ?????? ?????????????????????.").build();
            return result;
        }

        ExpenseInfoEntity entity = eRepo.findById(eiSeq).get();

        if (data.getUpdatePrice() == null) {
            data.setUpdatePrice(entity.getEiPrice());
        }
        if (data.getUpdateDate() == null) {
            data.setUpdateDate(entity.getEiDate());
        }
        if (data.getUpdateCateSeq() == null) {
            data.setUpdateCateSeq(entity.getCategoryInfoEntity().getCateSeq());
        }
        if (data.getUpdateMemo() == null) {
            data.setUpdateMemo(entity.getEiMemo());
        }
        if (data.getUpdateStatus() == null) {
            data.setUpdateStatus(entity.getEiStatus());
        }
        if (data.getUpdatePrice() < 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ??????????????????.")
                    .build();
            return result;
        }
        if (cateRepo.countByCateSeq(data.getUpdateCateSeq()) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ???????????? ???????????????.")
                    .build();
            return result;
        }
        
        // Path folderLocation = Paths.get(accountbook_img);
        // AccountBookImgEntity data1 = null;
        // if (file != null) { // ????????? ??????????????????
        //     String originFileName = file.getOriginalFilename();
        //     // ????????? ????????? ???????????? ?????? ????????? ?????? ?????? ??????. ????????? ?????? ????????? 
        //     if (!originFileName.equals("")) {
        //         String[] split = originFileName.split("\\.");
        //         String ext = split[split.length - 1];
        //         String filename = "";
        //         for (int i = 0; i < split.length - 1; i++) {
        //             filename += split[i];
        //             // ???????????? ??????
        //         }
        //         if (entity.getAccountBookImgEntity() != null) {
        //             String deleteFileName = entity.getAccountBookImgEntity().getAiImgName();
        //             Path targetDeleteFile = folderLocation.resolve(deleteFileName);
        //             try {
        //                 Files.delete(targetDeleteFile);
        //             } catch (Exception e) {
        //                 e.printStackTrace();
        //             }
        //             String saveFileName = "account" + "-";
        //             Calendar c = Calendar.getInstance();
        //             saveFileName += c.getTimeInMillis() + "." + ext;
        //             Path targetFile = folderLocation.resolve(saveFileName);
        //             try {
        //                 Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        //             } catch (Exception e) {
        //                 e.printStackTrace();
        //             }
        //             data1 = entity.getAccountBookImgEntity();
        //             data1.setAiImgName(saveFileName);
        //             data1.setAiUri(filename);
        //             accountBookImgService.addAccountBookImgInfo(data1);
        //         } else {
        //             String saveFileName = "account" + "-";
        //             Calendar c = Calendar.getInstance();
        //             saveFileName += c.getTimeInMillis() + "." + ext;
        //             Path targetFile = folderLocation.resolve(saveFileName);
        //             try {
        //                 Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        //             } catch (Exception e) {
        //                 e.printStackTrace();
        //             }

        //             data1 = new AccountBookImgEntity();
        //             data1.setAiImgName(saveFileName);
        //             data1.setAiUri(filename);
        //             accountBookImgService.addAccountBookImgInfo(data1);
        //         }
        //     }
        // } else {
        //     data1 = entity.getAccountBookImgEntity();
        // }
        AccountBookImgEntity data1 = null;
        
        String fileName = CommonUtils.buildFileName(file.getOriginalFilename());
    
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw null;
          }
            data1 = new AccountBookImgEntity();
            data1.setAiImgName(fileName);
            data1.setAiUri(fileName);
            accountBookImgService.addAccountBookImgInfo(data1);   

        entity.setEiStatus(data.getUpdateStatus());
        entity.setEiMemo(data.getUpdateMemo());
        entity.setEiPrice(data.getUpdatePrice());
        entity.setEiDate(data.getUpdateDate());
        entity.setCategoryInfoEntity(cateRepo.findById(data.getUpdateCateSeq()).get());
        entity.setAccountBookImgEntity(data1);
        eRepo.save(entity);

        ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(true).message("?????? ????????? ?????????????????????.")
                .build();
        return result;
    }

    // ?????? ????????? ?????? 
    public ExpenseInfoResponseVO deleteExpenseImg(Long eiSeq) {
        if (eRepo.countByEiSeq(eiSeq) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ?????? ?????? ???????????????.")
                    .build();
            return result;
        }
        ExpenseInfoEntity entity = eRepo.findById(eiSeq).get();
        if (entity.getAccountBookImgEntity() == null) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ???????????? ????????????.")
                    .build();
            return result;
        }
        Path folderLocation = Paths.get(accountbook_img);
        String deleteFileName = entity.getAccountBookImgEntity().getAiImgName();
        Path targetDeleteFile = folderLocation.resolve(deleteFileName);
        try {
            Files.delete(targetDeleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        accounImgRepo.delete(entity.getAccountBookImgEntity());
        entity.setAccountBookImgEntity(null);
        eRepo.save(entity);
        ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(true).message("???????????? ?????????????????????.")
                .build();
        return result;

    }

    // ?????? ?????? ?????? 
    public ExpenseInfoResponseVO deleteImportInfo(Long iiSeq) {
        if (iRepo.countByIiSeq(iiSeq) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ?????? ?????? ???????????????.")
                    .build();
            return result;
        }
        ImportInfoEntity entity = iRepo.findById(iiSeq).get();
        iRepo.delete(entity);
        ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(true).message(iiSeq + "?????? ??????????????? ??????????????????.")
                .build();
        return result;
    }

    // ?????? ?????? ??????
    public ExpenseInfoResponseVO deleteExpenseInfo(Long eiSeq) {
        if (eRepo.countByEiSeq(eiSeq) == 0) {
            ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(false).message("???????????? ?????? ?????? ?????? ???????????????.")
                    .build();
            return result;
        }
        ExpenseInfoEntity entity = eRepo.findById(eiSeq).get();
        if (entity.getAccountBookImgEntity() != null) {
            Path folderLocation = Paths.get(accountbook_img);
            String deleteFileName = entity.getAccountBookImgEntity().getAiImgName();
            Path targetDeleteFile = folderLocation.resolve(deleteFileName);
            try {
                Files.delete(targetDeleteFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            accounImgRepo.delete(entity.getAccountBookImgEntity());
        }
            eRepo.delete(entity);
             ExpenseInfoResponseVO result = ExpenseInfoResponseVO.builder().status(true).message(eiSeq+"?????? ??????????????? ??????????????????.")
                    .build();
            return result;
    }
}