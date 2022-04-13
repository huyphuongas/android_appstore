package gst.trainingcourse.firebasedemo.repository

import android.app.Application
import gst.trainingcourse.firebasedemo.dao.AppDao
import gst.trainingcourse.firebasedemo.database.AppDatabase
import gst.trainingcourse.firebasedemo.entity.Person

class PersonRepository(application: Application) {
    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    fun insertPerson(person: Person) = appDao.insertPerson(person)

    fun checkPhonePassword(phone: String, password: String) =
        appDao.checkPhonePassword(phone, password)

    fun findPersonByPhone(phone: String) = appDao.findPersonByPhone(phone)
    fun updatePerson(person: Person) = appDao.updatePerson(person)
    fun deletePerson(phone: String) = appDao.deletePerson(phone)

    fun findPersonByID(personID: String) = appDao.findPersonByID(personID)


}