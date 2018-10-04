package com.example.fulltest.domain

import com.example.fulltest.domain.Player
import spock.lang.Specification

class PlayerCreatingTest extends Specification {

    def "creating empty player with out validation"() {
        given:
        def player = new Player()
        expect:
        player.getName() == null
        player.getAge() == null
        player.getId() == null
        player == new Player()
    }


    def "creating normal player with out validation"() {
        given:
        def player = new Player(1L, 'Vova', 30L)
        expect:
        player.getName() == 'Vova'
        player.getAge() == 30L
        player.getId() == 1L
        player == new Player(1L, 'Vova', 30L)
    }

    def "player setters test with out validation"() {
        given:
        def player = new Player(1L, 'Vova', 30L)
        when:
        player.setId(2L)
        player.setName('Pasha')
        player.setAge(31L)
        then:
        player.getName() == 'Pasha'
        player.getAge() == 31L
        player.getId() == 2L
        player == new Player(2L, 'Pasha', 31L)
    }

    def "players should not be equal with dif properties with out validation"() {
        given:
        def player1 = new Player(1L, 'Vova', 30L)
        def player2 = new Player(2L, 'Pasha', 31L)
        expect:
        player1 != player2
    }
}
