package com.example.outsourcing.common.initializer;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MultipartInitializer {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @PostConstruct
  public void initUploadDirectory() {
    try {
      Path path = Paths.get(System.getProperty("user.dir")).resolve(uploadDir);
      // 디렉토리 없다면 새로 생성
      if (!Files.exists(path)) {
        Files.createDirectories(path);
      }
    } catch (IOException e) {
      throw new RuntimeException("업로드 디렉토리 생성에 실패하였습니다.", e);
    }
  }
}
