package uz.gita.myemailauthapp1.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData

interface HomeViewModel {
    val errorLiveData : LiveData<String>
    val progressLiveData : LiveData<Boolean>
    val goToLoginScreenLiveData : LiveData<Unit>
    fun setImageUri(uri: Uri)
    fun getImageUri() : String
    fun progressBar(showProgressBar: Boolean)
    fun logOut()

}