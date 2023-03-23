package uz.gita.myemailauthapp1.presentation.viewmodel

import androidx.lifecycle.LiveData

interface SplashViewModel {

    val openNextScrenLiveData : LiveData<Unit>
    val openLoginScrenLiveData : LiveData<Unit>
}