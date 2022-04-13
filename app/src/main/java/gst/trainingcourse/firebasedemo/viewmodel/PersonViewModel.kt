package gst.trainingcourse.firebasedemo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.firebasedemo.entity.Person
import gst.trainingcourse.firebasedemo.repository.FireStoreRepository
import gst.trainingcourse.firebasedemo.repository.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonViewModel(application: Application) : ViewModel() {
    private val personRepository: PersonRepository = PersonRepository(application)
    private val fireStoreRepository = FireStoreRepository(application)

    class FactoryViewModel(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PersonViewModel(application) as T
            }
            throw IllegalAccessException("Unable construct viewModel")
        }
    }

    fun insertPerson(person: Person) = personRepository.insertPerson(person)
    fun checkPhonePass(phone: String, pass: String) =
        personRepository.checkPhonePassword(phone, pass)

    fun findPersonByPhone(phone: String) = personRepository.findPersonByPhone(phone)
    private fun updatePerson(person: Person) = personRepository.updatePerson(person)
    fun deletePerson(phone: String) = personRepository.deletePerson(phone)
    fun addFireStore(person: Person) = fireStoreRepository.addFireStore(person)
    fun getDataFromFireStore() = viewModelScope.launch() {
        val list = fireStoreRepository.getDataFromFireStore()
        list.forEach { person -> personRepository.insertPerson(person) }
    }

    fun updateDataToFireStore(person: Person) = viewModelScope.launch(Dispatchers.IO) {
        fireStoreRepository.updateDataToFireStore(person)
        updatePerson(person)
    }

    fun deletePersonFromFireStore(person: Person) =
        fireStoreRepository.deletePersonFromFireStore(person)
}