package com.example.outsourcing.image.util;

import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.image.service.ImageService;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@RequiredArgsConstructor
public class ImageUtil {

  private final ImageService imageService;

  // 이미지 조회
  public ResponseEntity<Resource> getImage(@RequestParam Long imageId) {
    if (imageId == null) {
      return ResponseEntity.notFound().build();
    }

    Image image = imageService.getImage(imageId);
    Path path = Path.of(image.getPath());

    // 이미지 파일에서 확장자 추출
    String fileExtension = getFileExtension(image.getPath());
    MediaType mediaType = getMediaType(fileExtension);

    // 경로로 들어가 파일 불러오기
    Resource resource = new FileSystemResource(path.toFile());

    return ResponseEntity.ok()
        .contentType(mediaType)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "inline; filename=\"" + image.getPath() + "\"")   // 바디에 바로 이미지 보이도록
        .body(resource);
  }

  // 파일 확장자 추출
  private String getFileExtension(String filename) {
    String extension = "";
    int i = filename.lastIndexOf('.');

    if (i > 0) {  // 확장자가 존재한다면 (. 이 포함되어있다면)
      extension = filename.substring(i + 1);
    }

    return extension;
  }

  // 파일 확장자와 미디어 타입 매치
  private MediaType getMediaType(String extension) {
    switch (extension.toLowerCase()) {
      case "jpg":
      case "jpeg":
        return MediaType.IMAGE_JPEG;                // jpg, jpeg

      case "png":
        return MediaType.IMAGE_PNG;                 // png

      default:
        return MediaType.APPLICATION_OCTET_STREAM;  // 바이너리 파일
    }
  }
}
