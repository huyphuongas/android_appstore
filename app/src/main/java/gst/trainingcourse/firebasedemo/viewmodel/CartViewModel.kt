package gst.trainingcourse.firebasedemo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gst.trainingcourse.firebasedemo.entity.Cart
import gst.trainingcourse.firebasedemo.repository.CartRepository

class CartViewModel(application: Application) : ViewModel() {
    private val cartRepository = CartRepository(application)

    class FactoryViewModel(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(application) as T
            }
            throw IllegalAccessException("Unable construct viewModel")
        }
    }

    fun addToCart(cart: Cart) = cartRepository.addToCart(cart)
    fun getAllCart(personID: String) = cartRepository.getAllCart(personID)
    fun searchCart(personID: String) = cartRepository.searchCart(personID)
    fun deleteCart(personID: String,productID: String) = cartRepository.deleteCart(personID,productID)
    fun searchListCartCheckout(personID: String) = cartRepository.searchListCartCheckout(personID)
    fun updateCartNumber(quantity: Int, personID: String, productID: String) =
        cartRepository.updateCartNumber(quantity, personID, productID)
    fun checkoutCart(id : String) = cartRepository.checkoutCart(id)
    fun updateCartCheckout(personID: String, productID: String) =
        cartRepository.updateCartCheckout(personID, productID)


}