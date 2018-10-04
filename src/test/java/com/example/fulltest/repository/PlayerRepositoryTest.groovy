package com.example.fulltest.repository

import com.example.fulltest.domain.Player
import com.example.fulltest.service.ExternalAPIClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.mock.mockito.MockBean
import spock.lang.Specification

import javax.transaction.Transactional

@DataJpaTest
@Transactional
class PlayerRepositoryTest extends Specification {

    @MockBean
    private ExternalAPIClient externalAPIClient

    @Autowired
    private TestEntityManager testEntityManager

    @Autowired
    private PlayerRepository playerRepository

    def "should save player and return same but with id"() {
        given:
        def player = new Player(null, 'Vova', 29L)
        when:
        def repoPersistedPlayer = playerRepository.save(player)
        then:
        repoPersistedPlayer.getId() != null
        cleanup:
        playerRepository.deleteAll()
    }

    def "should update player and return same"() {
        given:
        def player = new Player(null, 'Vova', 29L)
        def updatedPlayer = new Player(1L, 'Pasha', 29L)
        when:
        playerRepository.save(player)
        def repoUpdatedPlayer = playerRepository.save(updatedPlayer)
        then:
        repoUpdatedPlayer.getName() == 'Pasha'
        cleanup:
        playerRepository.deleteAll()
    }

    def "should get all players"() {
        given:
        def list = Arrays.asList(new Player(null, 'Vova', 29L), new Player(null, 'Pasha', 30L))
        when:
        playerRepository.saveAll(list)
        def allPlayers = playerRepository.findAll()
        then:
        allPlayers.size() == 2
        allPlayers.get(0).getName() == 'Vova'
        allPlayers.get(1).getName() == 'Pasha'
        cleanup:
        playerRepository.deleteAll()
    }

    def "should get all players but with where clause"() {
        given:
        def list = playersList

        when:
        playerRepository.saveAll(list)
        def allPlayers = playerRepository.findAll()

        then:
        allPlayers.size() == size

        where:
        playersList                                                              || size
        Arrays.asList(new Player(1L, 'Vova', 29L), new Player(2L, 'Pasha', 30L)) || 2
        Arrays.asList(new Player(1L, 'Vova', 29L))                               || 1
        Arrays.asList()                                                          || 0
    }

    def "should delete one player"() {
        given:
        def player = new Player(null, 'Vova', 29L)

        when:
        playerRepository.save(player)
        playerRepository.delete(player)

        then:
        playerRepository.findAll().size() == 0
        cleanup:
        playerRepository.deleteAll()
    }

    def "should delete all players"() {
        given:
        def list = Arrays.asList(new Player(1L, 'Vova', 29L), new Player(2L, 'Pasha', 30L))

        when:
        playerRepository.saveAll(list)
        playerRepository.deleteAll()

        then:
        playerRepository.findAll().size() == 0
        cleanup:
        playerRepository.deleteAll()
    }
}
