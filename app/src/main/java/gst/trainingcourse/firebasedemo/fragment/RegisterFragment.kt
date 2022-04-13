package gst.trainingcourse.firebasedemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gst.trainingcourse.firebasedemo.R
import gst.trainingcourse.firebasedemo.databinding.FragmentRegisterBinding
import gst.trainingcourse.firebasedemo.entity.Person
import gst.trainingcourse.firebasedemo.validate.Validate
import gst.trainingcourse.firebasedemo.viewmodel.PersonViewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val personViewModel: PersonViewModel by lazy {
        ViewModelProvider(
            this,
            PersonViewModel.FactoryViewModel(requireActivity().application)
        )[PersonViewModel::class.java]
    }



    private var _binding: FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction() {

        binding.btnRegister.setOnClickListener {
            addPersonOnclick()
        }
    }

    private fun addPersonOnclick() {
        val valid = Validate()
        val name = binding.edtRegisterName.text.toString()
        val email = binding.edtRegisterEmail.text.toString()
        val phone = binding.edtRegisterPhone.text.toString()
        val address = binding.edtRegisterAddress.text.toString()
        val pass = binding.edtRegisterPassword.text.toString()
        val repass = binding.edtRegisterConfirmPassword.text.toString()
        val gender = if (binding.cbNam.isChecked) "1" else "0"
        if (!valid.checkPass(pass)) {
            binding.edtRegisterPassword.text = null
            Toast.makeText(context, "Invalid Password!", Toast.LENGTH_SHORT).show()
        } else if (pass != repass) {
            binding.edtRegisterPassword.text = null
            binding.edtRegisterConfirmPassword.text = null
            Toast.makeText(context, "RePassword is not same Password!", Toast.LENGTH_SHORT)
                .show()
        } else if (!valid.isValidEmail(email)) {
            Toast.makeText(context, "Invalid Email!", Toast.LENGTH_SHORT).show()
        } else if (!valid.validCellPhone(phone)) {
            Toast.makeText(context, "Invalid Phone!", Toast.LENGTH_SHORT).show()
        } else {
            val person = Person(phone, name, email, phone, pass, address, gender, 0)
            personViewModel.addFireStore(person)
            personViewModel.insertPerson(person)
            val action = RegisterFragmentDirections.actionRegisterFragment2ToLoginFragment()
            findNavController().navigate(action)
        }
    }
}