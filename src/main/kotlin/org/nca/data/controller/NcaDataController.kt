package org.nca.data.controller

import org.nca.data.dto.NcaDataDTO
import org.nca.data.service.NcaDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author stanislav.lapitsky created 5/30/2017.
 */

@RestController
class NcaDataController {
    @Autowired
    lateinit var ncaDataService: NcaDataService

    @RequestMapping(
            path = arrayOf("/nca-data"),
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun nameRequest(
            @RequestParam(value = "startDate")
            @DateTimeFormat(pattern="yyyy.MM.dd")
            startDate: Date,
            @RequestParam(value = "endDate")
            @DateTimeFormat(pattern="yyyy.MM.dd")
            endDate: Date): List<NcaDataDTO> {

        val list = ncaDataService.getDate(startDate, endDate)
        println(list)
        return list
    }

    @RequestMapping(
            path = arrayOf("/nca-month-data"),
            method = arrayOf(RequestMethod.GET, RequestMethod.POST),
            produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun getMonthData(
            @RequestParam(value = "year")
            year: Int,
            @RequestParam(value = "month")
            month: Int): List<List<Any>> {

        val list = ncaDataService.getMonthData(year, month)
        println(list)
        return list
    }

    @RequestMapping(
            path = arrayOf("/clear-file"),
            method = arrayOf(RequestMethod.GET, RequestMethod.POST),
            produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun clearFile(
            @RequestParam(value = "clearFileName")
            clearFileName: String): String {

        ncaDataService.clearFile(clearFileName)
        return "ok"
    }
}
