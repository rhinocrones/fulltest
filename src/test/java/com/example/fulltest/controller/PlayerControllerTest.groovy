package com.example.fulltest.controller

import com.example.fulltest.domain.Player
import com.example.fulltest.repository.PlayerRepository
import com.example.fulltest.service.ExternalAPIClient
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

import static groovy.json.JsonOutput.toJson
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [PlayerController])
class PlayerControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    @MockBean
    private ExternalAPIClient externalAPIClient

    @Autowired
    private PlayerRepository playerRepository

    @Autowired
    private ObjectMapper objectMapper

    @Unroll
    def "valid post test"() {
        given:
        def request = [
                id  : null,
                name: name,
                age : age
        ]

        and:
        playerRepository.save(new Player(null, name, age)) >> new Player(
                id,
                name,
                age
        )

        when:
        def results = mvc.perform(post('/players').contentType(APPLICATION_JSON).content(toJson(request)))

        then:
        results.andExpect(status().isCreated())

        and:
        results.andExpect(jsonPath('$.id').value(id))
        results.andExpect(jsonPath('$.name').value(name))
        results.andExpect(jsonPath('$.age').value(age))

        where:
        id || name    || age
        1L || 'Vova'  || 20L
        2L || 'Pasha' || 21L
    }

    @Unroll
    def "valid find by id test"() {
        setup:
        playerRepository.findById(id) >> Optional.of(new Player(id, name, age))

        when:
        def results = mvc.perform(get('/players/' + id).contentType(APPLICATION_JSON))

        then:
        results.andExpect(status().is2xxSuccessful())

        and:
        results.andExpect(jsonPath('$.id').value(id))
        results.andExpect(jsonPath('$.name').value(name))
        results.andExpect(jsonPath('$.age').value(age))

        where:
        id || name    || age
        1L || 'Vova'  || 20L
        2L || 'Pasha' || 21L
    }

    @Unroll
    def "valid find all test"() {
        setup:
        playerRepository.findAll() >> playersList

        when:
        def results = mvc.perform(get('/players').contentType(APPLICATION_JSON))

        then:
        results.andExpect(status().is2xxSuccessful())

        and:
        results.andExpect(content().json(response))


        where:
        playersList                                                              || response
        Arrays.asList(new Player(1L, 'Vova', 29L), new Player(2L, 'Pasha', 30L)) || "[{'id':1,'name':'Vova','age':29},{'id':2,'name':'Pasha','age':30}]"
        Arrays.asList(new Player(1L, 'Vova', 29L))                               || "[{'id':1,'name':'Vova','age':29}]"
        Arrays.asList()                                                          || "[]"
    }

    @Unroll
    def "valid put but with wrong id test"() {
        given:
        def request = [
                id  : id,
                name: name,
                age : age
        ]

        and:
        playerRepository.save(new Player(id, name, age)) >> Optional.of(new Player(id, name, age))

        when:
        def results = mvc.perform(put('/players/' + id).contentType(APPLICATION_JSON).content(toJson(request)))

        then:
        results.andExpect(status().isBadRequest())

        and:
        results.andExpect(content().contentType('text/plain;charset=UTF-8'))
        results.andExpect(content().string(exception))

        where:
        id || name    || age || exception
        1L || 'Vova'  || 20L || 'Wrong id for player'
        2L || 'Pasha' || 21L || 'Wrong id for player'
    }

    @Unroll
    def "valid put test"() {
        given:
        def request = [
                id  : id,
                name: updatedName,
                age : age
        ]

        and:
        playerRepository.findById(id) >> Optional.of(new Player(id, name, age))
        playerRepository.save(new Player(id, updatedName, age)) >> new Player(id, updatedName, age)

        when:
        def results = mvc.perform(put('/players/' + id).contentType(APPLICATION_JSON).content(toJson(request)))

        then:
        results.andExpect(status().is2xxSuccessful())

        and:
        results.andExpect(jsonPath('$.id').value(id))
        results.andExpect(jsonPath('$.name').value(updatedName))
        results.andExpect(jsonPath('$.age').value(age))

        where:
        id || name    || age || updatedName
        1L || 'Vova'  || 20L || 'Vova1'
        2L || 'Pasha' || 21L || 'Pasha1'
    }

    @Unroll
    def "valid delete but with wrong id test"() {
        setup:
        playerRepository.deleteById(id) >> void

        when:
        def results = mvc.perform(delete('/players/' + id).contentType(APPLICATION_JSON))

        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().string(exception))

        where:
        id || exception
        1L || 'Wrong id for player'
        2L || 'Wrong id for player'
    }

    @Unroll
    def "valid delete test"() {
        setup:
        playerRepository.findById(id) >> Optional.of(new Player(id, 'Vova', 30L))
        playerRepository.deleteById(id)

        when:
        def results = mvc.perform(delete('/players/' + id).contentType(APPLICATION_JSON))

        then:
        results.andExpect(status().isNoContent())

        where:
        id || exception
        1L || 'Wrong id for player'
        2L || 'Wrong id for player'
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        PlayerRepository playerRepository() {
            return detachedMockFactory.Stub(PlayerRepository)
        }
    }
}
