package uz.gita.myemailauthapp1.presentation.viewmodel.impl

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.myemailauthapp1.data.local.MySharedPref
import uz.gita.myemailauthapp1.domain.AppRepository
import uz.gita.myemailauthapp1.presentation.viewmodel.HomeViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val mySharedPref: MySharedPref,
    private val repository: AppRepository
): ViewModel(), HomeViewModel {
    override val errorLiveData = MutableLiveData<String>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val goToLoginScreenLiveData = MutableLiveData<Unit>()
    private val myAuth = Firebase.auth
    override fun setImageUri(uri: Uri) {
        progressLiveData.value = true
        mySharedPref.imageUri = uri.toString()
        repository.setImageUri(uri).onEach {
            progressLiveData.value = false
            it.onFailure { error ->
                errorLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun getImageUri(): String {
        return  mySharedPref.imageUri
    }

    override fun progressBar(showProgressBar: Boolean) {
        progressLiveData.value = showProgressBar
    }

    override fun logOut() {
        myAuth.currentUser?.delete()
        mySharedPref.isRegistered = false
        repository.logOut().onEach {
            it.onSuccess {
                Log.d("ttt","viewmodel log out")
                goToLoginScreenLiveData.value = Unit
            }
            it.onFailure { error ->
                Log.d("ttt","viewmodel log out error")
                errorLiveData.value = error.message
            }
        }.launchIn(viewModelScope)

    }

}