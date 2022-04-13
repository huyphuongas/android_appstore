package gst.trainingcourse.firebasedemo

import androidx.room.TypeConverter
import gst.trainingcourse.firebasedemo.entity.OrderStatus
import java.util.*

class Converters {
    @TypeConverter
    fun orderStatusToString(status: OrderStatus): String = status.toString()

    @TypeConverter
    fun stringToOrderStatus(status: String): OrderStatus = OrderStatus.valueOf(status)
}