package com.example.fulltest.controller

import com.example.fulltest.controller.ExternalResourceController
import com.example.fulltest.domain.Player
import com.example.fulltest.repository.PlayerRepository
import com.example.fulltest.service.ExternalAPIClient
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [ExternalResourceController])
class ExternalResourceControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    @Autowired
    private ExternalAPIClient externalAPIClient

    @Unroll
    def "valid find all test"() {
        setup:
        Mockito.when(externalAPIClient.findAll()).thenReturn(playersList)

        when:
        def results = mvc.perform(get('/resource').contentType(APPLICATION_JSON))

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
    def "valid find by id test"() {
        setup:
        Mockito.when(externalAPIClient.findById(id)).thenReturn(new Player(id, name, age))

        when:
        def results = mvc.perform(get('/resource/' + id).contentType(APPLICATION_JSON))

        then:
        results.andExpect(status().is2xxSuccessful())

        and:
        results.andExpect(jsonPath('$.id').value(id))
        results.andExpect(jsonPath('$.name').value(name))
        results.andExpect(jsonPath('$.age').value(age))

        where:
        id || name    || age
        1L || 'Vova'  || 30L
        2L || 'Pasha' || 31L
    }

    @Unroll
    def "find by invalid id test"() {
        setup:
        Mockito.when(externalAPIClient.findById(id)).thenReturn(new Player(id, name, age))

        when:
        def results = mvc.perform(get('/resource/' + id).contentType(APPLICATION_JSON))

        then:
        results.andExpect(status().is2xxSuccessful())

        and:
        results.andExpect(jsonPath('$.id').value(id))
        results.andExpect(jsonPath('$.name').value(name))
        results.andExpect(jsonPath('$.age').value(age))

        where:
        id || name    || age
        1L || 'Vova'  || 30L
        2L || 'Pasha' || 31L
    }
}
