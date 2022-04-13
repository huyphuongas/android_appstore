package gst.trainingcourse.firebasedemo.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.firebasedemo.R
import gst.trainingcourse.firebasedemo.dao.AppDao
import gst.trainingcourse.firebasedemo.database.AppDatabase
import gst.trainingcourse.firebasedemo.entity.Cart

class CartAdapter(
    application: Application,
    var listener: OnClickDelete,
    val btnPlus: OnClickPlus,
    val btnMinus: OnClickMinus,
    val btnCheckOut: OnClickCheckOut,
) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private var cart = listOf<Cart>()

    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(cart: Cart) {
            val productName = itemView.findViewById<TextView>(R.id.productName)
            val productImg = itemView.findViewById<ImageView>(R.id.productImg)
            val price = itemView.findViewById<TextView>(R.id.price)
            val product = appDao.findProductByID(cart.productID)
            val person = appDao.findPersonByID(cart.personID)
            val plus = itemView.findViewById<ImageView>(R.id.btn_plus)
            val minus = itemView.findViewById<ImageView>(R.id.btn_minus)
            val tvQuantity = itemView.findViewById<TextView>(R.id.quantity)
            val btnDelete = itemView.findViewById<TextView>(R.id.btn_delete)
            val cbProduct = itemView.findViewById<CheckBox>(R.id.cartCheckbox)

            productName.text = product.name
            Glide.with(itemView).load(product.image).into(productImg)
            price.text = (product.price).toString()
            tvQuantity.text = cart.quantity.toString()
            cbProduct.isChecked = cart.checkout
            plus.setOnClickListener {
                if (product.stock > cart.quantity) {
                    cart.quantity += 1
                    tvQuantity.text = cart.quantity.toString()
                }else{
                    Toast.makeText(itemView.context,"Out of stock",Toast.LENGTH_LONG).show()
                }
                btnPlus.onClickPlus(cart.quantity, person.personId, product.productID)
            }
            minus.setOnClickListener {
                if (cart.quantity > 1) {
                    cart.quantity -= 1
                    tvQuantity.text = cart.quantity.toString()
//                    price.text = (product.price * cart.quantity).toString()
                }
                btnMinus.onClickMinus(cart.quantity, person.personId, product.productID)
            }

            btnDelete?.setOnClickListener {
                listener.onClickDelete(person.personId,product.productID)
            }
            cbProduct.setOnClickListener {
                btnCheckOut.onClickCheckOut(cart.personID, cart.productID)
                cbProduct.isChecked = !cart.checkout
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.onBind(cart[position])
    }

    interface OnClickDelete {
        fun onClickDelete(personID: String,productID: String)
    }

    interface OnClickPlus {
        fun onClickPlus(quantity: Int, personID: String, productID: String)
    }

    interface OnClickMinus {
        fun onClickMinus(quantity: Int, personID: String, productID: String)
    }

    interface OnClickCheckOut {
        fun onClickCheckOut(personID: String, productID: String)
    }

    override fun getItemCount() = cart.size

    fun setProducts(cart: List<Cart>) {
        this.cart = cart
        notifyDataSetChanged()
    }
}