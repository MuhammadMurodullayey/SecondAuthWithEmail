package uz.gita.myemailauthapp1.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myemailauthapp1.R
import uz.gita.myemailauthapp1.databinding.ScreenSplashBinding
import uz.gita.myemailauthapp1.presentation.viewmodel.SplashViewModel
import uz.gita.myemailauthapp1.presentation.viewmodel.impl.SplashViewModelImpl

@AndroidEntryPoint
class SplashScreen: Fragment(R.layout.screen_splash) {
    private val viewModel : SplashViewModel by viewModels<SplashViewModelImpl>()
    private val binding by viewBinding(ScreenSplashBinding ::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding){
        super.onViewCreated(view, savedInstanceState)
        viewModel.openNextScrenLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_splashScreen_to_homeScreen)
        }
        viewModel.openLoginScrenLiveData.observe(this@SplashScreen,goToLoginScreenObserver)
    }
    private val goToLoginScreenObserver = Observer<Unit>{
        val bundle = Bundle()
        bundle.putBoolean("Splash",true)
        findNavController().navigate(R.id.action_splashScreen_to_loginScreen,bundle)
    }
}