package gst.trainingcourse.firebasedemo.fragment

import android.annotation.SuppressLint
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import gst.trainingcourse.firebasedemo.R
import gst.trainingcourse.firebasedemo.adapter.ProductAdapter
import gst.trainingcourse.firebasedemo.adapter.SliderAdapter
import gst.trainingcourse.firebasedemo.databinding.FragmentHomeBinding
import gst.trainingcourse.firebasedemo.entity.Person
import gst.trainingcourse.firebasedemo.entity.Product
import gst.trainingcourse.firebasedemo.viewmodel.ProductViewModel

class HomeFragment : Fragment(R.layout.fragment_home), ProductAdapter.OnClickImg {


    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(
            this,
            ProductViewModel.FactoryViewModel(requireActivity().application)
        )[ProductViewModel::class.java]
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        initView()
        sliderItems()
    }

    private fun sliderItems() {
        val sliderAdapter = SliderAdapter(this)
        binding.vpHotProduct.adapter = sliderAdapter
        productViewModel.getAllProduct().observe(viewLifecycleOwner, Observer {
            sliderAdapter.setItemSlider(it)
        })
    }

    private fun initView() {
        productViewModel.getProductFromFireStore()
        val adapter = ProductAdapter(this)
        binding.rvContacts.setHasFixedSize(true)
        binding.rvContacts.layoutManager = LinearLayoutManager(context)
        binding.rvContacts.adapter = adapter
        productViewModel.getAllProduct().observe(viewLifecycleOwner, Observer {
            adapter.setProducts(it)
        })
    }

    override fun onClickImg(product: Product) {
        val person = arguments?.getParcelable<Person>("person")
        val check = arguments?.getBoolean("check")
        if (person != null && check != true) {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                product = product,
                person = person
            )
            findNavController().navigate(action)
        } else {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragmentNotLogin(
                product = product,
                check = true
            )
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initAction() {
        val person = arguments?.getParcelable<Person>("person")
        val check = arguments?.getBoolean("check")

        val bottomNavigationView =
            view?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewHome)

        bottomNavigationView?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.personDetailFragment -> {
                    if (person == null || check == true) {
                        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                        findNavController().navigate(action)
                    } else {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToPersonDetailFragment(person = person)
                        findNavController().navigate(action)
                    }
                    return@setOnItemSelectedListener true

                }
                R.id.cartFragment -> {
                    if (check != true && person != null) {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToCartFragment(person = person)
                        findNavController().navigate(action)

                    } else {
                        Toast.makeText(context, "You need to Login first!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.homeFragment -> {
                    return@setOnItemSelectedListener true
                }
                R.id.search -> {
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }

            }

        }


        if (check != true && person != null) {
            binding.tvWelcomeUser.text = "Welcome " + person.name
        } else {
            binding.tvWelcomeUser.text = "Welcome to san_Shop"
        }


        binding.imvNam.setOnClickListener {
            val adapter = ProductAdapter(this)
            binding.rvContacts.setHasFixedSize(true)
            binding.rvContacts.layoutManager = LinearLayoutManager(context)
            binding.rvContacts.adapter = adapter
            productViewModel.getProductsByType("Quần Áo Nam").observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
        }

        binding.imvAll.setOnClickListener {
            val adapter = ProductAdapter(this)
            binding.rvContacts.setHasFixedSize(true)
            binding.rvContacts.layoutManager = LinearLayoutManager(context)
            binding.rvContacts.adapter = adapter
            productViewModel.getAllProduct().observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
        }
        binding.imvNu.setOnClickListener {
            val adapter = ProductAdapter(this)
            binding.rvContacts.setHasFixedSize(true)
            binding.rvContacts.layoutManager = LinearLayoutManager(context)
            binding.rvContacts.adapter = adapter
            productViewModel.getProductsByType("Quần Áo Nữ").observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
        }
        binding.imvShoes.setOnClickListener {
            val adapter = ProductAdapter(this)
            binding.rvContacts.setHasFixedSize(true)
            binding.rvContacts.layoutManager = LinearLayoutManager(context)
            binding.rvContacts.adapter = adapter
            productViewModel.getProductsByType("Giày Dép").observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
        }
    }

}


