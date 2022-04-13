package gst.trainingcourse.firebasedemo.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import gst.trainingcourse.firebasedemo.R
import gst.trainingcourse.firebasedemo.adapter.OrderAdapter
import gst.trainingcourse.firebasedemo.databinding.FragmentOrderBinding
import gst.trainingcourse.firebasedemo.entity.*
import gst.trainingcourse.firebasedemo.viewmodel.CartViewModel
import gst.trainingcourse.firebasedemo.viewmodel.OrderViewModel
import gst.trainingcourse.firebasedemo.viewmodel.ProductViewModel

class OrderFragment : Fragment(R.layout.fragment_order) {

    private var _binding: FragmentOrderBinding? = null
    private val binding
        get() = _binding!!

    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider(
            this,
            OrderViewModel.FactoryViewModel(requireActivity().application)
        )[OrderViewModel::class.java]
    }
    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(
            this,
            ProductViewModel.FactoryViewModel(requireActivity().application)
        )[ProductViewModel::class.java]
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            CartViewModel.FactoryViewModel(requireActivity().application)
        )[CartViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        val person = arguments?.getParcelable<Person>("person")
            if (person != null) {
                val adapter = OrderAdapter(Application())
                binding.rvOrder.layoutManager = LinearLayoutManager(context)
                binding.rvOrder.adapter = adapter
                cartViewModel.searchListCartCheckout(person.personId)
                    .observe(viewLifecycleOwner, Observer {
                        adapter.setOrders(it)
                    })
                binding.edtOrderName.setText(person.name)
                binding.edtOrderAddress.setText(person.address)
                binding.edtOrderPhone.setText(person.phone)
                var totalPrice = 0.toLong()
                val listCheckoutCart = cartViewModel.searchCart(person.personId)
                for (item in listCheckoutCart) {
                    val product = productViewModel.findProductByID(item.productID)
                    totalPrice += product.price * item.quantity
                    binding.tvOrderTotal.text = totalPrice.toString()
                }
            }
    }

    private fun initAction() {
        binding.tvOrderPayment.setOnClickListener {
            val person = arguments?.getParcelable<Person>("person")
            if (person != null && binding.tvOrderTotal.text.toString().toLong() != 0.toLong()) {
                val order = Order(person.personId, person.personId, OrderStatus.Pending)
                orderViewModel.insertOrder(order)
                val listCheckoutCart = cartViewModel.searchCart(person.personId)
                for (item in listCheckoutCart) {
                    if (item.checkout) {
                        val product = productViewModel.findProductByID(item.productID)
                        val personID = item.personID
                        val productID = item.productID
                        val quantity = item.quantity
                        val totalPrice = product.price * quantity
                        val newStock = product.stock - quantity
                        val orderItem =
                            OrderItem(personID, productID, quantity, totalPrice, product.discount)
                        orderViewModel.insertOrderItem(orderItem)
                        orderViewModel.addOrderItemToFireStore(orderItem)
                        cartViewModel.checkoutCart(personID)
                        productViewModel.updateStock(newStock,item.productID)
                        productViewModel.updateProductFireStore(product,newStock)
                    }
                }
                binding.tvOrderTotal.text = "0"
                Toast.makeText(context, "Order success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Nothing to order", Toast.LENGTH_SHORT).show()
            }

        }
    }

}