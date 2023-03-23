package uz.gita.myemailauthapp1.presentation.viewmodel.impl

import android.content.ContentResolver
import android.provider.Settings
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.myemailauthapp1.data.UserModel
import uz.gita.myemailauthapp1.data.local.MySharedPref
import uz.gita.myemailauthapp1.domain.AppRepository
import uz.gita.myemailauthapp1.presentation.viewmodel.RegisterViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
   private val repository: AppRepository,
   private val mySharedPref: MySharedPref
) : ViewModel(), RegisterViewModel {
    private val myAuth = Firebase.auth
    override val errorLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val goToNextScreenLiveData = MutableLiveData<Unit>()
    override val goToLoginScreenLiveData =  MutableLiveData<Unit>()

    override fun goToNextScreen() {
       goToNextScreenLiveData.value = Unit
    }

    override fun goToLoginScreen() {
        goToLoginScreenLiveData.value = Unit
    }

    override fun getUser() {
        repository.getUsers().onEach {
            it.onSuccess {

            }
            it.onFailure {

            }
        }.launchIn(viewModelScope)
    }
    override fun addUser(user: UserModel) {
        repository.addUser(user).onEach {
            it.onSuccess {
                progressLiveData.value = false
                goToNextScreenLiveData.value = Unit
            }
            it.onFailure {
                errorLiveData.value = it.message
            }
        }.launchIn(viewModelScope)
    }

    override fun registerUser(name: String, email: String, password: String, contentResolver: ContentResolver) {
        progressLiveData.value = true
        if (TextUtils.isEmpty(email)){
            errorLiveData.value = "Email cannot be empty"
        }else if (TextUtils.isEmpty(password)){
            errorLiveData.value = "Password cannot be empty"
        }else if (password.length < 6){
            errorLiveData.value =  "Password length cannot be less then 6 letter"
        }else{
            myAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {task ->
                    progressLiveData.value = false
                    if (task.isSuccessful){
                        errorLiveData.value = "User successfully registered"
                        val id: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                        addUser(UserModel(name,email,password,id))
                        goToLoginScreenLiveData.value  = Unit
                    }else{
                        errorLiveData.value = task.exception?.message
                    }
                }
        }

    }
}