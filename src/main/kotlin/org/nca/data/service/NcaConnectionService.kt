package org.nca.data.service

import org.nca.data.dto.NcaDataDTO
import org.nca.data.utils.asDateString
import org.nca.data.utils.dateParse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * @author stanislav.lapitsky created 5/30/2017.
 */
const val SESSION_ID_NAME = "atereq"
val NCA_MAIN_URL = "http://pr.nca.by/"
val NCA_URL = "http://pr.nca.by/{session}.a_request.show_prices_count_test.xml"
val NCA_BODY_TEMPLATE = "v_param=3&v_objnum=17030&v_date_begin={startDate}&v_date_end={endDate}&v_sq_begin=&v_sq_end=" +
        "&v_purpose=40101&v_soato=5000000000&v_typ=3&v_name=&v_pr_b={startPrice}&v_pr_e={endPrice}" +
        "&v_cur=&v_url=http//pr.nca.by/&v_room=&v_floor_beg=&v_floor_end=&v_floor_number_beg=&v_floor_number_end=" +
        "&v_input_date_beg=&v_input_date_end=&v_purpose_KSforIP=-1&v_wallmat="

val PRICE_MAX = 1000000;
//val PRICE_STEP = 1000;
val PRICE_STEP = 2000;
val RETRY_COUNT = 10;
//val PRICE_MAX = 100000;
//val PRICE_STEP = 10000;

open class NCAConnectionService {

    val restTemplate = RestTemplate()

    fun getSearchSessionId():String? {
        val response = restTemplate.exchange(NCA_MAIN_URL, HttpMethod.GET, HttpEntity.EMPTY, String::class.java)
        val cookie = response.headers.get("Set-Cookie")
                ?.firstOrNull()?.split(";")?.firstOrNull()?.split("=")?.lastOrNull()

        return cookie
    }

    fun getNCASearchResults(startDate:Date, endDate:Date):List<NcaDataDTO> {
        return getNCASearchResults(startDate, endDate, PRICE_MAX, PRICE_STEP)
    }
    fun getNCASearchResults(startDate:Date, endDate:Date, priceMax:Int, priceStep:Int):List<NcaDataDTO> {
        var retryCount = RETRY_COUNT
        var sessionId = getSearchSessionId()
        if(sessionId.isNullOrEmpty()) {
            return listOf()
        }
        else {
            val res = mutableListOf<NcaDataDTO>()
            var startPrice = 0
            var endPrice = startPrice + priceStep
            while (startPrice < priceMax) {
                try {
                    val dto = getNCASearchResult(startDate, endDate, startPrice, endPrice-1, sessionId!!)
                    if (dto.count >0) {
                        res.add(dto)
                    }
                    startPrice = endPrice
                    endPrice = startPrice + priceStep
                }
                catch (e:Exception) {
                    sessionId = getSearchSessionId()
                    retryCount--
                    if (retryCount<=0) {
                        break
                    }
                }
            }

            return res
        }
    }

    fun getNCASearchResult(startDate:Date, endDate:Date, startPrice:Int, endPrice:Int, ncaSessionId:String):NcaDataDTO {
        val count = getNCASearchResultCount(startDate, endDate, startPrice.toString(), endPrice.toString(), ncaSessionId)
        val dto = NcaDataDTO(startDate, endDate, startPrice, endPrice, count)

        return dto
    }

    fun getNCASearchResultCount(startDate:Date, endDate:Date, startPrice:String, endPrice:String, ncaSessionId:String):Int {
        val headers = HttpHeaders()
        headers.set("Accept","*/*")
        headers.set("Accept-Encoding","gzip, deflate")
        headers.set("Content-type","application/x-www-form-urlencoded")

        val bodyStr=NCA_BODY_TEMPLATE
                .replace("{startDate}", startDate.asDateString("dd.MM.yyyy"), false)
                .replace("{endDate}", endDate.asDateString("dd.MM.yyyy"), false)
                .replace("{startPrice}", startPrice, false)
                .replace("{endPrice}", endPrice, false)
        val request = HttpEntity(bodyStr, headers)


        val url = NCA_URL.replace("{session}", ncaSessionId, false)
        val response = restTemplate.exchange(url, HttpMethod.POST, request, String::class.java).body
        val startIndex=response.indexOf("<rowcount_all>") + "<rowcount_all>".length
        val endIndex=response.indexOf("</rowcount_all>")
        val strCount = response.substring(startIndex, endIndex)
//            println(response)

        return strCount.toInt()
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
//            println(NCAConnectionService().getSearchSessionId())
            println(NCAConnectionService().getNCASearchResults(dateParse("2017.01.01"), dateParse("2017.02.01")))
        }
    }
}