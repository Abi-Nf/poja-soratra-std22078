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

  public String save(String id, String value) {
    try {
      File originalFile = Files.createTempFile(id + originalPrefix, ".txt").toFile();
      FileWriter writer = new FileWriter(originalFile);
      writer.append(value);
      writer.close();
      bucket.upload(originalFile, id + originalPrefix);

      File transformFile = Files.createTempFile(id + transformPrefix, ".txt").toFile();
      FileWriter tWriter = new FileWriter(transformFile);
      tWriter.append(value.toUpperCase());
      tWriter.close();
      bucket.upload(transformFile, id + originalPrefix);
      boolean original = originalFile.delete();
      boolean transform = transformFile.delete();
      if (original && transform) {
        System.out.println("Yay !");
      }
      return null;
    } catch (Exception ignored) {
    }
    return null;
  }

  public Transform get(String id) {
    Duration limitTime = Duration.ofHours(4);
    String original = bucket.presign(id + originalPrefix, limitTime).toString();
    String transform = bucket.presign(id + transformPrefix, limitTime).toString();
    return new Transform(original, transform);
  }
}
