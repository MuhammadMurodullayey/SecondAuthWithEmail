package uz.gita.myemailauthapp1.presentation.viewmodel

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import uz.gita.myemailauthapp1.data.UserModel

interface RegisterViewModel  {
    val errorLiveData : LiveData<String>
    val progressLiveData : LiveData<Boolean>
    val goToNextScreenLiveData : LiveData<Unit>
    val goToLoginScreenLiveData : LiveData<Unit>

    fun registerUser(name : String, email : String, password :String,contentResolver: ContentResolver)
    fun goToNextScreen()
    fun goToLoginScreen()
    fun getUser()
    fun addUser(user : UserModel)
}