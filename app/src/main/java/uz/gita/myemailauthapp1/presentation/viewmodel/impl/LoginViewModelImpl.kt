package uz.gita.myemailauthapp1.presentation.viewmodel.impl

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.myemailauthapp1.data.UserModel
import uz.gita.myemailauthapp1.data.local.MySharedPref
import uz.gita.myemailauthapp1.domain.AppRepository
import uz.gita.myemailauthapp1.presentation.viewmodel.LoginViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
     @ApplicationContext context: Context,
    private val repository: AppRepository,
    private val mySharedPref: MySharedPref
) : ViewModel(), LoginViewModel {
    @SuppressLint("StaticFieldLeak")
    private val appContext = context
    private val myAuth = Firebase.auth
    override val errorLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val goToNextScreenLiveData = MutableLiveData<Int>()
    override val showDialogLiveData = MutableLiveData<List<UserModel>>()

    override fun goToRegisterScreen() {
        goToNextScreenLiveData.value = 0
    }

    override fun goToHomeScreen() {
        goToNextScreenLiveData.value = 1
    }


    fun showDialog(){
        progressLiveData.value = true
        repository.getUsers().onEach {
            progressLiveData.value = false
            it.onSuccess { list1 ->
                val id: String = Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)
                Log.d("ttt","${id} current id")
                showDialogLiveData.value = list1.filter { user ->
                    Log.d("ttt","${user.deviseSerialNumber} users id")
                         user.deviseSerialNumber == id

                }
            }
            it.onFailure { error ->
                errorLiveData.value = error.message
            }
        }.launchIn(viewModelScope)

    }

    override fun autoLogin() {
        showDialog()
    }

    override fun setLocalData(uri: String, email: String) {
        mySharedPref.imageUri = uri
        mySharedPref.email = email
    }

    override fun loginUser(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            errorLiveData.value = "Email cannot be empty"
        } else if (TextUtils.isEmpty(password)) {
            errorLiveData.value = "Password cannot be empty"
        } else {
            progressLiveData.value = true
            myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mySharedPref.email = email
                    progressLiveData.value = false
                    repository.getUsers().onEach { result ->
                        progressLiveData.value = false
                        result.onSuccess { list1 ->
                            val currentUser = list1.find { it.email == email }
                            mySharedPref.imageUri = currentUser?.imageUri?:""
                            mySharedPref.email = currentUser?.email?:""
                            mySharedPref.isRegistered = true
                            goToHomeScreen()
                        }
                        result.onFailure { error ->
                            errorLiveData.value = error.message
                        }
                    }.launchIn(viewModelScope)

                } else {
                    progressLiveData.value = false
                    errorLiveData.value = task.exception?.message
                }

            }
        }

    }
}