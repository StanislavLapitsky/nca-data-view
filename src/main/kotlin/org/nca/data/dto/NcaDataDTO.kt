package org.nca.data.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

/**
 * @author stanislav.lapitsky created 5/30/2017.
 */

data class NcaDataDTO (@get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd") val startDate: Date,
                       @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd") val endDate: Date,
                       val startPrice: Int,
                       val endPrice: Int,
                       val count: Int)