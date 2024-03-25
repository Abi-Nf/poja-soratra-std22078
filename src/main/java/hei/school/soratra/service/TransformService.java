package hei.school.soratra.service;

import hei.school.soratra.DTO.Transform;
import hei.school.soratra.file.BucketComponent;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransformService {
  private final BucketComponent bucket;

  private static final String originalPrefix = "--original";
  private static final String transformPrefix = "--transform";

  public String save(String id, String value){
    try {
      File originalFile = Files.createTempFile(id + originalPrefix, null).toFile();
      FileWriter writer = new FileWriter(originalFile);
      writer.append(value.toLowerCase());
      writer.close();
      bucket.upload(originalFile, id + originalPrefix);

      File transformFile = Files.createTempFile(id + transformPrefix, null).toFile();
      writer = new FileWriter(transformFile);
      writer.append(value.toUpperCase());
      writer.close();
      bucket.upload(transformFile, id + transformPrefix);
      return null;
    }catch (Exception ignored){}
    return null;
  }

  public Transform get(String id) {
    Duration limitTime = Duration.ofHours(4);
    String original = bucket.presign(id + originalPrefix, limitTime).toString();
    String transform = bucket.presign(id + transformPrefix, limitTime).toString();
    return new Transform(original, transform);
  }
}
