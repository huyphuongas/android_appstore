package gst.trainingcourse.firebasedemo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gst.trainingcourse.firebasedemo.entity.Order
import gst.trainingcourse.firebasedemo.entity.OrderItem
import gst.trainingcourse.firebasedemo.repository.*

class OrderViewModel(application: Application) : ViewModel() {

    private val orderRepository = OrderRepository(application)
    private val fireStoreRepository = FireStoreRepository(application)

    class FactoryViewModel(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(application) as T
            }
            throw IllegalAccessException("Unable construct viewModel")
        }
    }


    fun insertOrderItem(orderItem: OrderItem) = orderRepository.insertOrderItem(orderItem)
    fun insertOrder(order: Order) = orderRepository.insertOrder(order)
    fun addOrderItemToFireStore(orderItem: OrderItem) = fireStoreRepository.addOrderItemToFireStore(orderItem)

}