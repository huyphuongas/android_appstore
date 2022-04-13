package gst.trainingcourse.firebasedemo.repository

import android.app.Application
import gst.trainingcourse.firebasedemo.dao.AppDao
import gst.trainingcourse.firebasedemo.database.AppDatabase
import gst.trainingcourse.firebasedemo.entity.Cart
import gst.trainingcourse.firebasedemo.entity.Order
import gst.trainingcourse.firebasedemo.entity.OrderItem

class CartRepository(application: Application) {

    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    fun addToCart(cart: Cart) = appDao.addToCart(cart)
    fun getAllCart(personID:String) = appDao.getAllCart(personID)
    fun searchListCartCheckout(personID: String) = appDao.searchListCartCheckout(personID)
    fun searchCart(personID: String) = appDao.searchCart(personID)
    fun updateCartCheckout(personID: String,productID: String) = appDao.updateCartCheckout(personID, productID)
    fun updateCartNumber(quantity:Int,personID: String,productID:String) = appDao.updateCartNumber(quantity, personID, productID)
    fun checkoutCart(id : String) = appDao.checkoutCart(id)
    fun deleteCart(personID: String,productID: String) = appDao.deleteItemFromCart(personID,productID)

}