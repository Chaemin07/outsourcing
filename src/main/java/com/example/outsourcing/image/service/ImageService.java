package com.example.outsourcing.image.service;

import static com.example.outsourcing.common.exception.ErrorCode.FAILED_DELETE_FILE;
import static com.example.outsourcing.common.exception.ErrorCode.FAILED_WRITE_FILE;
import static com.example.outsourcing.common.exception.ErrorCode.NOT_FOUND_FILE;
import static com.example.outsourcing.common.exception.ErrorCode.UNSUPPORTED_MEDIA_TYPE;

import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.image.repository.ImageRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageRepository imageRepository;
  @Value("${file.upload-dir}")
  private String RESOURCE_DIR;
  private final List<String> ALLOWED_FILE_TYPES = List.of(
      ".jpeg", ".jpg", ".png"
  );

  // 단일 파일 저장 후 이미지 반환
  @Transactional(rollbackFor = IOException.class)
  public Image uploadImage(MultipartFile image) {
    // 파일 타입 검사
    if (!isValidFileType(image.getOriginalFilename())) {
      throw new BaseException(UNSUPPORTED_MEDIA_TYPE);
    }

    // 파일 이름 중복 방지
    String uuidFilename = UUID.randomUUID() + "_" + image.getOriginalFilename();

    // 파일 저장 경로 설정
    Path filePath = Paths.get(RESOURCE_DIR + uuidFilename);
    try {
      Files.write(filePath, image.getBytes()); // TODO : 핸들러로 빼서 병행 처리?
    } catch (IOException e) {
      log.info("FAILED TO WRITE FILE PATH: {}", filePath.toString());
      throw new BaseException(FAILED_WRITE_FILE);
    }

    // image 엔티티에 경로 등등 정보 저장
    Image img = new Image(filePath.toString());
    imageRepository.save(img);

    // 이미지 반환
    return img;
  }

  // 파일 아이디로 조회
  public Image getImage(Long imageId) {
    return imageRepository.findById(imageId).orElseThrow(
        () -> new BaseException(NOT_FOUND_FILE));
  }

  // 파일 삭제
  @Transactional(rollbackFor = IOException.class)
  public void deleteImage(Long imageId) {
    Image image = imageRepository.findById(imageId).orElseThrow(
        () -> new BaseException(NOT_FOUND_FILE));
    Path path = Path.of(image.getPath());
    imageRepository.delete(image);

    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new BaseException(FAILED_DELETE_FILE);
    }
  }

  // 파일 확장자 검사
  private boolean isValidFileType(String uploadFileType) {
    for (String fileType : ALLOWED_FILE_TYPES) {
      if (uploadFileType.toLowerCase().endsWith(fileType)) {
        System.out.println("FILE TYPE: " + uploadFileType);
        return true;
      }
    }
    return false;
  }
}
