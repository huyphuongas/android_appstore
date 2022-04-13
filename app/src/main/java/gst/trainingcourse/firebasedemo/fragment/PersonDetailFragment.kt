package gst.trainingcourse.firebasedemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gst.trainingcourse.firebasedemo.databinding.FragmentPersonDetailBinding
import gst.trainingcourse.firebasedemo.entity.Person
import gst.trainingcourse.firebasedemo.viewmodel.OrderViewModel
import gst.trainingcourse.firebasedemo.viewmodel.PersonViewModel


class PersonDetailFragment : Fragment() {

    private val personViewModel: PersonViewModel by lazy {
        ViewModelProvider(
            this,
            PersonViewModel.FactoryViewModel(requireActivity().application)
        )[PersonViewModel::class.java]
    }

    private var _binding: FragmentPersonDetailBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        val person = arguments?.getParcelable<Person>("person")
        binding.edtName.setText(person?.name)
        binding.edtEmail.setText(person?.email)
        binding.edtPhone.setText(person?.phone)
        binding.edtAddress.setText(person?.address)
        if (person?.gender == "1") {
            binding.cbNam.isChecked = true
            binding.cbNu.isChecked = false
        } else {
            binding.cbNam.isChecked = false
            binding.cbNu.isChecked = true
        }
    }

    private fun initAction() {
        val person = arguments?.getParcelable<Person>("person")
        binding.btnLogout.setOnClickListener {
            val action = PersonDetailFragmentDirections.actionPersonDetailFragmentToHomeFragment(
                check = true,
                person = person!!
            )
            findNavController().navigate(action)
        }
        binding.btnUpdate.setOnClickListener {
            val pass = person?.password
            val gender = if (binding.cbNam.isChecked) "1" else "0"
            val user = Person(
                binding.edtPhone.text.toString(),
                binding.edtName.text.toString(),
                binding.edtEmail.text.toString(),
                binding.edtPhone.text.toString(),
                pass!!,
                binding.edtAddress.text.toString(),
                gender,
                0
            )
            personViewModel.updateDataToFireStore(user)

            val action = PersonDetailFragmentDirections.actionPersonDetailFragmentToHomeFragment(
                check = false,
                person = user
            )
            findNavController().navigate(action)
        }
        binding.btnDelete.setOnClickListener {
            if (person != null) {
                personViewModel.deletePersonFromFireStore(person)
                personViewModel.deletePerson(person.phone)
                val action =
                    PersonDetailFragmentDirections.actionPersonDetailFragmentToHomeFragment(
                        check = true,
                        person = person
                    )
                findNavController().navigate(action)
            }
        }
    }


}