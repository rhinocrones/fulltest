package com.example.fulltest.controller;

import com.example.fulltest.domain.Player;
import com.example.fulltest.repository.PlayerRepository;
import java.util.Collection;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/players")
@RestController
@AllArgsConstructor
public class PlayerController {

  private PlayerRepository playerRepository;

  @GetMapping("/{id}")
  public Player findById(@PathVariable Long id) {
    return playerRepository.findById(id).orElseThrow(PlayerNotFoundException::new);
  }

  @GetMapping
  public Collection<Player> findAll() {
    return playerRepository.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Player save(@Valid @RequestBody Player player) {
    return playerRepository.save(player);
  }

  @PutMapping("/{id}")
  public Player update(@Valid @RequestBody Player player, @PathVariable Long id) {
    playerRepository.findById(id).orElseThrow(PlayerNotFoundException::new);
    return playerRepository.save(player);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    playerRepository.findById(id).orElseThrow(PlayerNotFoundException::new);
    playerRepository.deleteById(id);
  }
}
