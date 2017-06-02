package org.nca.data.service

import org.nca.data.dto.MonthDataChartItemDTO
import org.nca.data.dto.NcaDataDTO
import org.nca.data.utils.asDateString
import org.nca.data.utils.dateParse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.*
import java.util.*

/**
 * @author stanislav.lapitsky created 5/30/2017.
 */

const val NCA_FILE_PREFIX = "nca_data_"
const val AGGREGATE_STEP = 10000
@Service
class NcaDataServiceImpl : NcaDataService {
    @Value("\${nca.base.file.path:}")
    lateinit var baseFilePath:String

    override fun getDate(startDate: Date, endDate: Date): List<NcaDataDTO> {
//        val res:List<NcaDataDTO> = mutableListOf()
        val dataFileName = getNcaDataFileName(startDate, endDate)
        if (!File(dataFileName).exists()) {
            fillFile(startDate, endDate)
        }

        val res = readDataFile(File(dataFileName))
        return res;
    }

    override fun getMonthData(year: Int, month: Int): List<List<Any>> {
        val root = File(baseFilePath)
        val prefix = NCA_FILE_PREFIX + year + "." + if (month<=9) "0" + month else month
        for (f in root.listFiles()) {
            if (f.name.startsWith(prefix)) {
                return getAggregatedMonthData(readDataFile(f))
            }
        }
        val res = mutableListOf<List<Any>>()

        return res
    }

    private fun getAggregatedMonthData(source:List<NcaDataDTO>): List<List<Any>> {
        val res = mutableListOf<List<Any>>()
        var rangeStart = 0
        var accumulateSum = 0
        for (dto in source) {
            if (dto.startPrice <= rangeStart + AGGREGATE_STEP - 1) {
                accumulateSum += dto.count
            }
            else {
                res.add(listOf("" + rangeStart + "-" + (rangeStart + AGGREGATE_STEP - 1) , accumulateSum))
                accumulateSum = 0
                rangeStart += AGGREGATE_STEP
            }
        }

        return res
    }
    private fun getNcaDataFileName(startDate: Date, endDate: Date): String {
        val dataFileName = baseFilePath + NCA_FILE_PREFIX + startDate.asDateString() + "_" + endDate.asDateString()
        return dataFileName
    }

    fun fillFile(startDate: Date, endDate: Date): Boolean {
        val res = NCAConnectionService().getNCASearchResults(startDate, endDate)

        val file = File(getNcaDataFileName(startDate, endDate))
        fillFile(res, file)
        return true
    }

    fun readDataFile(file:File): List<NcaDataDTO> {
        val res:MutableList<NcaDataDTO> = mutableListOf()
        val reader = BufferedReader(FileReader(file))
        for (line in reader.lines()) {
            val mbrProperties = line.split("\t")
            res.add(NcaDataDTO(dateParse(mbrProperties[0]),
                    dateParse(mbrProperties[1]),
                    mbrProperties[2].toInt(),
                    mbrProperties[3].toInt(),
                    mbrProperties[4].toInt()))
        }
        return res
    }

    fun fillFile(dataList:List<NcaDataDTO>, file:File) {
        val writer = BufferedWriter(FileWriter(file))
        for (dto in dataList) {
            writer.write(dto.startDate.asDateString())
            writer.write("\t")
            writer.write(dto.endDate.asDateString())
            writer.write("\t")
            writer.write(dto.startPrice.toString())
            writer.write("\t")
            writer.write(dto.endPrice.toString())
            writer.write("\t")
            writer.write(dto.count.toString())
            writer.write("\n")
        }
        writer.flush()
        writer.close()
    }
}