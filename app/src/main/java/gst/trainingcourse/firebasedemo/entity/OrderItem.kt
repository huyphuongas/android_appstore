package gst.trainingcourse.firebasedemo.entity
import androidx.room.*
import gst.trainingcourse.firebasedemo.database.DatabaseConstants.ORDER_ITEM_TABLE
import androidx.room.ForeignKey
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.QUANTITY
import gst.trainingcourse.firebasedemo.database.DatabaseConstants.ORDER_ID
import gst.trainingcourse.firebasedemo.database.DatabaseConstants.PRODUCT_ID

@Entity(tableName = ORDER_ITEM_TABLE, primaryKeys = [ORDER_ID, PRODUCT_ID],
        foreignKeys = [androidx.room.ForeignKey(
    entity = Order::class,
    parentColumns = arrayOf(ORDER_ID),
    childColumns = arrayOf(ORDER_ID),
    onDelete = ForeignKey.CASCADE
)])

data class OrderItem(
    @ColumnInfo(name = ORDER_ID) val orderID: String,
    @ColumnInfo(name = PRODUCT_ID) val productID: String,
    @ColumnInfo(name = QUANTITY) val quantity: Int,
    val price: Long,
    val discount: Double
)

data class OrderWithItems(
    @Embedded val order: Order ,
    @Relation(
        parentColumn = ORDER_ID,
        entityColumn = ORDER_ID
    )
    val orderItemLists: List<OrderItem>
)
