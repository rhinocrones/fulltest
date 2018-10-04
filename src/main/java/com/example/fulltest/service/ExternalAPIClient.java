package com.example.fulltest.service;

import com.example.fulltest.domain.Player;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "external-player-service", path = "/players")
public interface ExternalAPIClient {

  @GetMapping
  List<Player> findAll();

  @GetMapping("/{id}")
  ResponseEntity<Player> findById(@PathVariable("id") final Long id);
}
