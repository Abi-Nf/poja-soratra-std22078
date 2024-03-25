package hei.school.soratra.endpoint.rest.controller;

import hei.school.soratra.DTO.Transform;
import hei.school.soratra.service.TransformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/soratra")
public class SoratraController {
  private final TransformService service;

  @PutMapping("/{id}")
  public String saveSoratra(
    @PathVariable String id,
    @RequestBody String value
  ){
    String lowerCase = value.toLowerCase();
    return service.save(id, lowerCase);
  }

  @GetMapping("/{id}")
  public Transform getSoratra(
    @PathVariable String id
  ){
    return service.get(id);
  }
}