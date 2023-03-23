package uz.gita.myemailauthapp1.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.gita.myemailauthapp1.data.UserModel

interface LoginViewModel {
    val errorLiveData : LiveData<String>
    val progressLiveData : LiveData<Boolean>
    val goToNextScreenLiveData : LiveData<Int>
    val showDialogLiveData: LiveData<List<UserModel>>

    fun goToRegisterScreen()
    fun goToHomeScreen()
    fun loginUser(email : String,password :String)
    fun autoLogin()
    fun setLocalData(uri: String,email: String)
}