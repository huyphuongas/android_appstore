package gst.trainingcourse.firebasedemo.repository

import android.app.Application
import gst.trainingcourse.firebasedemo.dao.AppDao
import gst.trainingcourse.firebasedemo.database.AppDatabase
import gst.trainingcourse.firebasedemo.entity.Product

class ProductRepository(application: Application) {

    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    fun getAllProducts() = appDao.getProducts()
    fun insertProduct(product: Product) = appDao.insertProduct(product)
    fun findProductsByType(key: String) = appDao.findProductByType(key)
    fun findProductByID(id : String) = appDao.findProductByID(id)
    fun updateStock(quantity: Long, id:String) = appDao.updateStock(quantity,id)

}