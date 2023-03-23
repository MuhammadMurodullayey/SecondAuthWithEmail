package uz.gita.myemailauthapp1.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myemailauthapp1.R
import uz.gita.myemailauthapp1.data.UserModel
import uz.gita.myemailauthapp1.databinding.ScreenLoginBinding
import uz.gita.myemailauthapp1.presentation.dialogs.UserDialog
import uz.gita.myemailauthapp1.presentation.viewmodel.LoginViewModel
import uz.gita.myemailauthapp1.presentation.viewmodel.impl.LoginViewModelImpl

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
              val arg = arguments?.getBoolean("Splash")
        if (arg != null && arg){
            viewModel.autoLogin()
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.passET.text.toString().trim()
            viewModel.loginUser(email, password)
        }
        binding.registerText.setOnClickListener {
            viewModel.goToRegisterScreen()
        }


        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.goToNextScreenLiveData.observe(this@LoginScreen, goToNextScreenObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.showDialogLiveData.observe(viewLifecycleOwner, showDialogObserver)
    }

    private val errorObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
    }
    private val goToNextScreenObserver = Observer<Int> {
        if (it == 0) {
            findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
        } else {
            findNavController().navigate(R.id.action_loginScreen_to_homeScreen)
        }
    }
    private val showDialogObserver = Observer<List<UserModel>> {
        if (it.isNotEmpty()) {
            val dialog = UserDialog(it) { user ->
                user.password?.let { it1 -> user.email?.let { it2 -> viewModel.loginUser(it2, it1) } }
                user.imageUri?.let { it1 -> user.email?.let { it2 -> viewModel.setLocalData(it1, it2) } }
            }
            dialog.show(childFragmentManager, "AutoLoginDialog")
        }else{
            Log.d("ttt","empty auto login")
        }
    }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }
}