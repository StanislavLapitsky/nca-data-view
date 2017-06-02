package org.nca.data

/**
 * @author stanislav.lapitsky created 5/30/2017.
 */
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.nca.data.utils.dateParse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import java.util.Calendar
import org.nca.data.utils.asDateString

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    @Ignore
    fun findFuture() {
        val res = restTemplate.getForEntity("/nca-data?" +
                "startDate=2050.01.01" +
                "&endDate=2050.02.01", String::class.java)
        assertEquals(res.statusCode, HttpStatus.OK)
        val content = """[{"startDate":"2049.12.31","endDate":"2050.01.31","startPrice":0,"endPrice":1,"count":1},
        {"startDate":"2049.12.31","endDate":"2050.01.31","startPrice":1,"endPrice":2,"count":2}]"""
        assertEquals(content, res.body)
    }

    @Test
    @Ignore
    fun fillMonth() {
        var startDate = dateParse("2016.07.01")
        while (startDate.before(Date())) {
            val endDate = getEndDate(startDate)
            val res = restTemplate.getForEntity("/nca-data?" +
                    "startDate=" + startDate.asDateString() +
                    "&endDate=" + endDate.asDateString(), String::class.java)
            startDate = getNextDate(startDate)
            assertEquals(res.statusCode, HttpStatus.OK)
        }
    }

    @Test
    @Ignore
    fun findMonth() {
        val res = restTemplate.getForEntity("/nca-month-data?" +
                    "year=2016" +
                    "&month=7", String::class.java)
        assertEquals(res.statusCode, HttpStatus.OK)
    }

    fun getEndDate(startDate:Date): Date {
        val cal = Calendar.getInstance()
        cal.time = startDate
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DATE, -1)
        return cal.time
    }

    fun getNextDate(startDate:Date): Date {
        val cal = Calendar.getInstance()
        cal.time = startDate
        cal.add(Calendar.MONTH, 1)
        return cal.time
    }

}