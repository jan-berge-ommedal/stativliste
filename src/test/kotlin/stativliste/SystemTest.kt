package stativliste

import io.kotlintest.matchers.collections.shouldHaveAtLeastSize
import io.kotlintest.matchers.string.shouldHaveMinLength
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpStatus

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class SystemTest {

    @LocalServerPort
    private lateinit var serverPort: Integer

    @Test
    fun stations() {
        val response = RestTemplate().getForEntity("http://localhost:$serverPort/api/stations", List::class.java)
        response.statusCode.shouldBe(HttpStatus.OK)
        val stations = response.body
        stations.shouldHaveAtLeastSize(200)
    }

    @Test
    fun index() {
        val response = RestTemplate().getForEntity("http://localhost:$serverPort", String::class.java)
        response.statusCode.shouldBe(HttpStatus.OK)
        val index = response.body
        index.shouldHaveMinLength(5000)
    }

}
