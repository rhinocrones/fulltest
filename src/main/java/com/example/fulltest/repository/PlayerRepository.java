package com.example.fulltest.repository;

import com.example.fulltest.domain.Player;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {

  @Override
  List<Player> findAll();
}
