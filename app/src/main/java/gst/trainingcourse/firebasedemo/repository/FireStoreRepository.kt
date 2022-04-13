package gst.trainingcourse.firebasedemo.repository

import android.app.Application
import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.ADDRESS
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.BALANCE
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.DESCRIPTION
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.DISCOUNT
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.EMAIL
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.GENDER
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.IMAGE
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.MESS_ERROR
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.MESS_EXITED
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.MESS_SUCCESS
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.NAME
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.ORDERS
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.ORDER_ID
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.PASSWORD
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.PHONE
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.PRICE
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.PRODUCTS
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.PRODUCT_ID
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.QUANTITY
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.STOCK
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.TOTAL_PRICE
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.TYPE
import gst.trainingcourse.firebasedemo.constants.FireStoreConstants.USERS
import gst.trainingcourse.firebasedemo.entity.OrderItem
import gst.trainingcourse.firebasedemo.entity.Person
import gst.trainingcourse.firebasedemo.entity.Product
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class FireStoreRepository(application: Application) {

    private val db = Firebase.firestore

    fun addFireStore(person: Person) {
        val user = HashMap<String, Any>()
        user[NAME] = person.name
        user[EMAIL] = person.email
        user[PHONE] = person.phone
        user[ADDRESS] = person.address
        user[PASSWORD] = person.password
        user[GENDER] = person.gender
        user[BALANCE] = 0
        var check = false
        db.collection(USERS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == person.phone) {
                        check = true
                        break
                    }
                }
                if (!check) {
                    db.collection(USERS).document(person.phone)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, MESS_SUCCESS)
                            //Toast.makeText(this, "Success adding account", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, MESS_ERROR, e)
                        }
                } else {
                    //Toast.makeText(this, "ID is exited", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, MESS_EXITED)
                }
            }
    }

    suspend fun getDataFromFireStore(): ArrayList<Person> {
        return suspendCoroutineWithTimeout(10000L) { listPersons ->
            val list = ArrayList<Person>()
            db.collection(USERS)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        val person = Person(document.id,
                            document.data[NAME].toString(),
                            document.data[EMAIL].toString(),
                            document.data[PHONE].toString(),
                            document.data[PASSWORD].toString(),
                            document.data[ADDRESS].toString(),
                            document.data[GENDER].toString(),
                            0
                        )
                        list.add(person)
                    }
                    listPersons.resume(list)
                }
        }
    }

    suspend fun updateDataToFireStore(person: Person) {
        return suspendCoroutineWithTimeout(10000L) {
            val updates = hashMapOf<String, Any>(
                NAME to person.name,
                EMAIL to person.email,
                ADDRESS to person.address,
                GENDER to person.gender,
            )
            db.collection(USERS)
                .document(person.phone)
                .update(updates)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG,
                        MESS_SUCCESS)
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG,
                        MESS_ERROR,
                        e)
                }
        }
    }
    suspend fun updateProductToFireStore(product: Product,stock :Long) {
        return suspendCoroutineWithTimeout(10000L) {
            val updates = hashMapOf<String, Any>(
                STOCK to stock,
            )
            db.collection(PRODUCTS)
                .document(product.productID)
                .update(updates)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG,
                        MESS_SUCCESS)
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG,
                        MESS_ERROR,
                        e)
                }
        }
    }


    fun deletePersonFromFireStore(person: Person) {
        db.collection(USERS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == person.phone) {
                        db.collection(USERS).document(person.phone)
                            .delete()
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG,
                                    MESS_SUCCESS)
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG,
                                    MESS_ERROR,
                                    e)
                            }
                    }
                }
            }
    }

    suspend fun getProductFromFireStore(): ArrayList<Product> {
        val list = arrayListOf<Product>()
        return suspendCoroutineWithTimeout(10000L) { listP ->
            db.collection(PRODUCTS)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        val product = Product(document.id,
                            document.data[NAME].toString(),
                            document.data[DESCRIPTION].toString(),
                            document.data[IMAGE].toString(),
                            document.data[PRICE] as Long,
                            document.data[DISCOUNT] as Double,
                            document.data[STOCK] as Long,
                            document.data[TYPE].toString()
                        )
                        list.add(product)
                    }
                    listP.resume(list)
                }

        }
    }

    fun addOrderItemToFireStore(orderItem: OrderItem) {
        val order = HashMap<String, Any>()
        order[ORDER_ID] = orderItem.orderID
        order[PRODUCT_ID] = orderItem.productID
        order[QUANTITY] = orderItem.quantity
        order[TOTAL_PRICE] = orderItem.price
        order[DISCOUNT] = orderItem.discount
        db.collection(ORDERS).document(orderItem.orderID).collection(orderItem.productID)
            .add(order)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, MESS_SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, MESS_ERROR, e)
            }
    }


    private suspend inline fun <T> suspendCoroutineWithTimeout(
        timeout: Long,
        crossinline block: (Continuation<T>) -> Unit,
    ) = withTimeout(timeout) {
        suspendCancellableCoroutine(block = block)
    }
}