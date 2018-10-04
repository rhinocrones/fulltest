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
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

import static groovy.json.JsonOutput.toJson
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [PlayerController])
class PlayerValidationTest extends Specification {

    @Autowired
    protected MockMvc mvc

    @Autowired
    private PlayerRepository playerRepository

    @Autowired
    private ObjectMapper objectMapper

    @MockBean
    private ExternalAPIClient externalAPIClient

    @Unroll
    def "should not allow to create a player with an invalid name: #name"() {
        given:
        def request = [
                id  : null,
                name: name,
                age : 29L
        ]

        when:
        def results = mvc.perform(post('/players').contentType(APPLICATION_JSON).content(toJson(request))).andReturn()

        then:
        results.response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()

        and:
        results.response.status == status

        where:
        name || status
        ""   || 422
        null || 422
    }

    @Unroll
    def "should not allow to create a player with an invalid age: #age"() {
        given:
        def request = [
                id  : null,
                name: 'Vova',
                age : age
        ]

        when:
        def results = mvc.perform(post('/players').contentType(APPLICATION_JSON).content(toJson(request))).andReturn()

        then:
        results.response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()

        and:
        results.response.status == status

        where:
        age || status
        0L  || 422
        17L || 422
    }

    @Unroll
    def "should allow to create a player with an valid name and age"() {
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
    def "should not allow to create a player with an invalid fields"() {
        given:
        Map request = [
                id  : null,
                name: name,
                age : age
        ]

        when:
        def results = mvc.perform(post('/players').contentType(APPLICATION_JSON).content(toJson(request)))

        then:
        results.andExpect(status().isUnprocessableEntity())
        results.andExpect(content().string(exception))

        where:
        name || age || exception
        null || 10L || 'You have some issue with request body'
        ""   || 20L || 'You have some issue with request body'
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
