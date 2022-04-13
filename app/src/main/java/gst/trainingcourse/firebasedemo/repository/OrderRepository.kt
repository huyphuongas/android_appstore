package gst.trainingcourse.firebasedemo.repository

import android.app.Application
import gst.trainingcourse.firebasedemo.dao.AppDao
import gst.trainingcourse.firebasedemo.database.AppDatabase
import gst.trainingcourse.firebasedemo.entity.Order
import gst.trainingcourse.firebasedemo.entity.OrderItem

class OrderRepository(application: Application) {
    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    fun insertOrderItem(orderItem: OrderItem) = appDao.insertOrderItems(orderItem)
    fun insertOrder(order: Order) = appDao.insertOrder(order)
}