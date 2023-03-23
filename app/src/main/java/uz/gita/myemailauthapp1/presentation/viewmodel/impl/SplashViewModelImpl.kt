package uz.gita.myemailauthapp1.presentation.viewmodel.impl

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.myemailauthapp1.data.local.MySharedPref
import uz.gita.myemailauthapp1.presentation.viewmodel.SplashViewModel
import javax.inject.Inject
@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    mySharedPref: MySharedPref
): ViewModel(), SplashViewModel {
    override val openNextScrenLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val openLoginScrenLiveData =  MutableLiveData<Unit>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            Log.d("ttt","${mySharedPref.isRegistered}")
            if (mySharedPref.isRegistered){
                openNextScrenLiveData.postValue(Unit)
            }else{
                openLoginScrenLiveData.postValue(Unit)
            }
        }
    }

}