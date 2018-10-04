package com.example.fulltest.controller;

import com.example.fulltest.domain.Player;
import com.example.fulltest.service.ExternalAPIClient;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/resource")
@RestController
@AllArgsConstructor
public class ExternalResourceController {

  private ExternalAPIClient externalAPIClient;

  @GetMapping
  public List<Player> findAll() {
    return externalAPIClient.findAll();
  }

  @GetMapping("/{id}")
  public Player findByid(@PathVariable Long id){
    return externalAPIClient.findById(id);
  }
}
