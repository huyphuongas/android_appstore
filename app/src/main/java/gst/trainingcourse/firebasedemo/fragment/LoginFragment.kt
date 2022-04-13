package gst.trainingcourse.firebasedemo.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gst.trainingcourse.firebasedemo.databinding.FragmentLoginBinding
import gst.trainingcourse.firebasedemo.viewmodel.PersonViewModel


class LoginFragment : Fragment() {



    private val personViewModel: PersonViewModel by lazy {
        ViewModelProvider(
            this,
            PersonViewModel.FactoryViewModel(requireActivity().application)
        )[PersonViewModel::class.java]
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initAction()
    }

    private fun initData() {
        personViewModel.getDataFromFireStore()
    }

    private fun initAction() {
        binding.btnLogin.setOnClickListener {
            val phone = binding.edtLoginPhone.text.toString()
            val pass = binding.edtPassword.text.toString()

            if (phone.isBlank() || pass.isBlank()) {
                Toast.makeText(context, "Phone or Password must not empty!", Toast.LENGTH_SHORT)
                    .show()
            } else if (!personViewModel.checkPhonePass(phone, pass)) {
                Toast.makeText(context, "Invalid Person", Toast.LENGTH_SHORT).show()
            } else {
                val person = personViewModel.findPersonByPhone(phone)
                val action =
                    LoginFragmentDirections.actionLoginFragmentToHomeFragment(person = person)
                findNavController().navigate(action)
                Toast.makeText(context, "Login Successfully!", Toast.LENGTH_SHORT).show()

            }
        }

        binding.btnLoginRegister.setOnClickListener {

            Log.d(TAG, "OnClick")

            Log.d(TAG, "OnClick")

            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment2()
            findNavController().navigate(action)
        }
    }
}