package uz.gita.myemailauthapp1.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myemailauthapp1.R
import uz.gita.myemailauthapp1.databinding.ScreenRegisterBinding
import uz.gita.myemailauthapp1.presentation.viewmodel.RegisterViewModel
import uz.gita.myemailauthapp1.presentation.viewmodel.impl.RegisterViewModelImpl


@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {
    private val binding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel: RegisterViewModel by viewModels<RegisterViewModelImpl>()
    private var name = ""
    var email = ""
    var password = ""

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            email = binding.emailEt.text.toString().trim()
            password = binding.passET.text.toString().trim()
            name = binding.name.text.toString().trim()
            viewModel.registerUser(name, email, password,requireContext().contentResolver)
        }
        binding.loginText.setOnClickListener {
            viewModel.goToLoginScreen()
        }


        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.goToNextScreenLiveData.observe(this@RegisterScreen, goToNextScreenObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.goToLoginScreenLiveData.observe(this@RegisterScreen, goToLoginScreenObserver)
    }

    private val goToLoginScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }
    private val errorObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
    }
    private val goToNextScreenObserver = Observer<Unit> {
    }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }


}