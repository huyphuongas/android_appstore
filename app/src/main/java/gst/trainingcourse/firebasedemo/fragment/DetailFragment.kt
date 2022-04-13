package gst.trainingcourse.firebasedemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import gst.trainingcourse.firebasedemo.R
import gst.trainingcourse.firebasedemo.databinding.FragmentDetailBinding
import gst.trainingcourse.firebasedemo.entity.*
import gst.trainingcourse.firebasedemo.viewmodel.CartViewModel
import gst.trainingcourse.firebasedemo.viewmodel.OrderViewModel

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            CartViewModel.FactoryViewModel(requireActivity().application)
        )[CartViewModel::class.java]
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        val product = arguments?.getParcelable<Product>("product")
        binding.tvName.text = product?.name
        binding.tvPrice.text = product?.price.toString()
        binding.tvDescription.text = product?.description
        binding.tvDetailTitle.text = product?.name
        context?.let { Glide.with(it).load(product?.image).into(binding.imvProduct) }
    }


    private fun initAction() {
        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        val person = arguments?.getParcelable<Person>("person")
        val check = arguments?.getBoolean("check")
        val bottomNavigationView =
            view?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewDetail)
        bottomNavigationView?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.cartFragment -> {
                    val product = arguments?.getParcelable<Product>("product")
                    if (check != true) {
                        val cart = Cart(person!!.personId, product!!.productID, 1, false)
                        cartViewModel.addToCart(cart)
                        val action =
                            DetailFragmentDirections.actionDetailFragmentToCartFragment(person = person)
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(context, "You need to Login first!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.orderFragment -> {
                    val product = arguments?.getParcelable<Product>("product")
                    val action = product?.let { Product ->
                        DetailFragmentDirections.actionDetailFragmentToOrderFragment(
                            check = true,
                            product = Product
                        )
                    }
                    if (action != null) {
                        findNavController().navigate(action)
                    }
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }
}