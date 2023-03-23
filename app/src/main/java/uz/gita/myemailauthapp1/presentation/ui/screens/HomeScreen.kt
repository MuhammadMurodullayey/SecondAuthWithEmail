package uz.gita.myemailauthapp1.presentation.ui.screens

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myemailauthapp1.R
import uz.gita.myemailauthapp1.databinding.ScreenHomeBinding
import uz.gita.myemailauthapp1.presentation.viewmodel.HomeViewModel
import uz.gita.myemailauthapp1.presentation.viewmodel.impl.HomeViewModelImpl


@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.screen_home) {
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels<HomeViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // requestPermissions(arrayOf("android.permission.READ_EXTERNAL_STORAGE"), 100)
        if (!checkPermission()) {
            requestPermission()
        } else {
            setImage()
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.goToLoginScreenLiveData.observe(this@HomeScreen, goToLoginScreenObserver)

        binding.button.setOnClickListener {
            if (checkPermission()) {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
            } else {
                requestPermission()
            }

        }

        binding.logOut.setOnClickListener {
            viewModel.logOut()
        }
    }

    private val errorObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private val goToLoginScreenObserver = Observer<Unit> {
        Log.d("ttt","goto register")
        findNavController().navigate(R.id.action_homeScreen_to_registerScreen)
    }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }


    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(requireContext().applicationContext, READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        requestPermissions(arrayOf("android.permission.READ_EXTERNAL_STORAGE"), 100)
        //ActivityCompat.requestPermissions(requireActivity(), arrayOf(READ_EXTERNAL_STORAGE), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val imageUri = data?.data
            viewModel.setImageUri(imageUri ?: Uri.EMPTY)
            setImage()
            Log.d("ttt", "setImageFragment")
        }
    }

    fun setImage() {
        viewModel.progressBar(true)
        val imageUri = viewModel.getImageUri()
        Log.d("ttt", "image uri in home $imageUri")
        if (imageUri != "") {
            Glide.with(binding.image)
                .load(imageUri)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        viewModel.progressBar(false)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewModel.progressBar(false)
                        return false
                    }

                })
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_error)
                .into(binding.image)


        } else {
            viewModel.progressBar(false)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> if (grantResults.isNotEmpty()) {
                val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (storageAccepted) {
                    Snackbar.make(
                        view!!,
                        "Permission Granted, Now you can access external storage",
                        Snackbar.LENGTH_LONG
                    ).show()
                    setImage()
                } else {
                    Snackbar.make(view!!, "Permission Denied, You cannot access external storage", Snackbar.LENGTH_LONG).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                            showMessageOKCancel(
                                "You need to allow access to both the permissions"
                            ) { _, _ ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                        arrayOf(READ_EXTERNAL_STORAGE),
                                        100
                                    )
                                }
                            }
                            return
                        }
                    }
                }
            }
        }

    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

}