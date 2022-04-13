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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import gst.trainingcourse.firebasedemo.R
import gst.trainingcourse.firebasedemo.adapter.CartAdapter
import gst.trainingcourse.firebasedemo.databinding.FragmentCartBinding
import gst.trainingcourse.firebasedemo.entity.Person
import gst.trainingcourse.firebasedemo.viewmodel.CartViewModel
import gst.trainingcourse.firebasedemo.viewmodel.ProductViewModel


class CartFragment : Fragment(R.layout.fragment_cart), CartAdapter.OnClickDelete,
    CartAdapter.OnClickPlus, CartAdapter.OnClickMinus,
    CartAdapter.OnClickCheckOut {

    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            CartViewModel.FactoryViewModel(requireActivity().application)
        )[CartViewModel::class.java]
    }

    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(
            this,
            ProductViewModel.FactoryViewModel(requireActivity().application)
        )[ProductViewModel::class.java]
    }

    private var _binding: FragmentCartBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        val person = arguments?.getParcelable<Person>("person")
        val adapter = CartAdapter(Application(), this, this, this, this)

        binding.rvCart.layoutManager = LinearLayoutManager(context)
        binding.rvCart.adapter = adapter
        if (person != null) {
            cartViewModel.getAllCart(person.personId).observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
            var totalPrice = 0.toLong()
            val listCheckoutCart = cartViewModel.searchCart(person.personId)
            for (item in listCheckoutCart) {
                val product = productViewModel.findProductByID(item.productID)
                totalPrice += product.price * item.quantity
                binding.tvCartTotal.text = totalPrice.toString()
            }
            if (listCheckoutCart.isNullOrEmpty()) {
                binding.tvCartTotal.text = "0"
            }
        }
    }

    override fun onClickDelete(personID: String, productID: String) {
        cartViewModel.deleteCart(personID, productID)
        var totalPrice = 0.toLong()
        val listCheckoutCart = cartViewModel.searchCart(personID)
        for (item in listCheckoutCart) {
            val product = productViewModel.findProductByID(item.productID)
            totalPrice += product.price * item.quantity
            binding.tvCartTotal.text = totalPrice.toString()
        }
        if (listCheckoutCart.isNullOrEmpty()) {
            binding.tvCartTotal.text = "0"
        }
    }

    override fun onClickPlus(quantity: Int, personID: String, productID: String) {
        cartViewModel.updateCartNumber(quantity, personID, productID)
        var totalPrice = 0.toLong()
        val listCheckoutCart = cartViewModel.searchCart(personID)
        for (item in listCheckoutCart) {
            val product = productViewModel.findProductByID(item.productID)
            totalPrice += product.price * item.quantity
            binding.tvCartTotal.text = totalPrice.toString()
        }
    }

    override fun onClickMinus(quantity: Int, personID: String, productID: String) {
        cartViewModel.updateCartNumber(quantity, personID, productID)
        var totalPrice = 0.toLong()
        val listCheckoutCart = cartViewModel.searchCart(personID)
        for (item in listCheckoutCart) {
            val product = productViewModel.findProductByID(item.productID)
            totalPrice += product.price * item.quantity
            binding.tvCartTotal.text = totalPrice.toString()
        }

    }


    override fun onClickCheckOut(personID: String, productID: String) {
        cartViewModel.updateCartCheckout(personID, productID)
        var totalPrice = 0.toLong()
        val listCheckoutCart = cartViewModel.searchCart(personID)
        for (item in listCheckoutCart) {
            val product = productViewModel.findProductByID(item.productID)
            totalPrice += product.price * item.quantity
            binding.tvCartTotal.text = totalPrice.toString()
        }
        if (listCheckoutCart.isNullOrEmpty()) {
            binding.tvCartTotal.text = "0"
        }
    }

    private fun initAction() {
        binding.tvCartPayment.setOnClickListener {
            val person = arguments?.getParcelable<Person>("person")
            if (person != null && binding.tvCartTotal.text.toString().toLong() != 0.toLong()) {
                val action =
                    CartFragmentDirections.actionCartFragmentToOrderFragment(person = person
                    )
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, "Nothing to order", Toast.LENGTH_SHORT).show()
            }
        }
    }


}