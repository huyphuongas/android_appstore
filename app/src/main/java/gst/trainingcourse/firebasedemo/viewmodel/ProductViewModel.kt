package gst.trainingcourse.firebasedemo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.firebasedemo.entity.Product
import gst.trainingcourse.firebasedemo.repository.FireStoreRepository
import gst.trainingcourse.firebasedemo.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : ViewModel() {
    private val productRepository = ProductRepository(application)
    private val fireStoreRepository = FireStoreRepository(application)

    class FactoryViewModel(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductViewModel(application) as T
            }
            throw IllegalAccessException("Unable construct viewModel")
        }
    }

    fun getProductFromFireStore() = viewModelScope.launch() {
        val list = fireStoreRepository.getProductFromFireStore()
        list.forEach { product -> productRepository.insertProduct(product) }
    }


    fun getAllProduct() = productRepository.getAllProducts()
    fun getProductsByType(key: String) = productRepository.findProductsByType(key)
    fun findProductByID(key: String) = productRepository.findProductByID(key)
    fun updateStock(quantity: Long, id:String) = productRepository.updateStock(quantity,id)
    fun updateProductFireStore(product: Product,stock : Long) = viewModelScope.launch() {
        fireStoreRepository.updateProductToFireStore(product,stock)
    }

}