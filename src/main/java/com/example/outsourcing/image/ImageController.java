package com.example.outsourcing.image;

import com.example.outsourcing.image.entity.Image;
import com.example.outsourcing.image.service.ImageService;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping("/profile")
  public ResponseEntity<Void> uploadProfile(@RequestParam MultipartFile image) {
    imageService.uploadImage(image);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/profile")
  public ResponseEntity<Resource> getProfile(@RequestParam Long imageId) {
    Image image = imageService.getImage(imageId);
    Path path = Path.of(image.getPath());

    // 이미지 파일에서 확장자 추출
    String fileExtension = getFileExtension(image.getPath());
    MediaType mediaType = getMediaType(fileExtension);

    Resource resource = new FileSystemResource(path.toFile());

    return ResponseEntity.ok()
        .contentType(mediaType)
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getPath() + "\"")
        .body(resource);
  }

  // 파일 확장자 추출 메소드
  private String getFileExtension(String filename) {
    String extension = "";
    int i = filename.lastIndexOf('.');
    if (i > 0) {
      extension = filename.substring(i + 1);
    }
    return extension;
  }

  // 확장자에 맞는 미디어 타입 반환
  private MediaType getMediaType(String extension) {
    switch (extension.toLowerCase()) {
      case "jpg":
      case "jpeg":
        return MediaType.IMAGE_JPEG;
      case "png":
        return MediaType.IMAGE_PNG;
      case "gif":
        return MediaType.IMAGE_GIF;
      default:
        return MediaType.APPLICATION_OCTET_STREAM; // 기본적으로 바이너리 파일로 처리
    }
  }

}
