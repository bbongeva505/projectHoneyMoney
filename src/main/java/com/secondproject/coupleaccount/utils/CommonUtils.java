package com.secondproject.coupleaccount.utils;

import java.nio.charset.StandardCharsets;

import org.springframework.http.ContentDisposition;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonUtils {
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    public static String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR); //파일 확장자 구분선
        String fileExtension = originalFileName.substring(fileExtensionIndex); //파일 확장자
        String fileName = originalFileName.substring(0, fileExtensionIndex); // 파일 이름
        String now = String.valueOf(System.currentTimeMillis()); // 파일 업로드 시간

        return fileName + "_" + now + fileExtension;
    }

    // public static String buildFileName(String category, String originalFileName) {
    //     int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
    //     String fileExtension = originalFileName.substring(fileExtensionIndex);
    //     String fileName = originalFileName.substring(0, fileExtensionIndex);
    //     String now = String.valueOf(System.currentTimeMillis());
    
    //     return category + CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + fileExtension;
    //   }

	private static final String CATEGORY_PREFIX = "/";
	private static final String TIME_SEPARATOR = "_";
  private static final int UNDER_BAR_INDEX = 1;

	public static ContentDisposition createContentDisposition(String categoryWithFileName) {
    String fileName = categoryWithFileName.substring(
        categoryWithFileName.lastIndexOf(CATEGORY_PREFIX) + UNDER_BAR_INDEX);
    return ContentDisposition.builder("attachment")
        .filename(fileName, StandardCharsets.UTF_8)
        .build();
  }
}

