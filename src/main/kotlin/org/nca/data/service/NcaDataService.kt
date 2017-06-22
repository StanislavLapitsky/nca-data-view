package org.nca.data.service

import org.nca.data.dto.MonthDataChartItemDTO
import org.nca.data.dto.NcaDataDTO
import java.util.*

/**
 * @author stanislav.lapitsky created 5/30/2017.
 */
interface NcaDataService {
    fun getDate(startDate:Date, endDate:Date): List<NcaDataDTO>
    fun getMonthData(year:Int, month:Int): List<List<Any>>
    fun clearFile(fileName:String)
}