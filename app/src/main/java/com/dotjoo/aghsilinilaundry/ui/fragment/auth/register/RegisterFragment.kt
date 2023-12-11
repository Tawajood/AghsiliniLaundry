package com.dotjoo.aghsilinilaundry.ui.fragment.auth.register

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.data.param.AddAddressParams
import com.dotjoo.aghsilinilaundry.databinding.FragmentRegisterBinding
import com.dotjoo.aghsilinilaundry.ui.activity.AuthActivity
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghsilinilaundry.ui.fragment.auth.login.AuthViewModel
import com.dotjoo.aghsilinilaundry.util.FileManager
import com.dotjoo.aghsilinilaundry.util.PermissionManager
import com.dotjoo.aghsilinilaundry.util.WWLocationManager
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.ext.isNull
import com.dotjoo.aghsilinilaundry.util.ext.loadImage
import com.dotjoo.aghsilinilaundry.util.ext.showActivity
import com.dotjoo.aghsilinilaundry.util.observe
import com.dotjoo.aghsilinilaundry.util.openLocationSettingsResultLauncher
import com.dotjoo.aghsilinilaundry.util.requestAppPermissions
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.iv_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import java.io.File
import javax.inject.Inject
@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private var logo: File? = null
    lateinit var parent: AuthActivity
    private val mViewModel: AuthViewModel by viewModels()
    var countryCode = "+20"
    var lat: String? = ""
    var lon: String? = ""
    var address: String? = ""

    @Inject
    lateinit var locationManager: WWLocationManager

    @Inject
    lateinit var permissionManager: PermissionManager
    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.RegisterSucess -> {
                showProgress(false)
                //  action.data.client?.social= action.social
                PrefsHelper.saveUserData(action.data)
                PrefsHelper.saveToken(action.data.token)
                goHome()

            }


            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    showToast(it.substring(3, it.length))
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            else -> {

            }
        }
    }


    private fun onClick() {
        parent = requireActivity() as AuthActivity
        binding.toolbar.tv_title.setText(resources.getString(R.string.new_user))
        binding.tvTermsandcondito.setPaintFlags(binding.tvTermsandcondito.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)


        binding.btnSignup.setOnClickListener {
            mViewModel.isVaildRegisteration(
                binding.etName.text.toString(),
                countryCode,
                binding.etPhone.text.toString(),
                lat, lon, address,
                //  binding.etAddress.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPasswordConfim.text.toString(),
                logo
            )
        }
        binding.btnSignin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)

        }
        binding.etAddress.setOnClickListener {
            checkLocation()
        }
        binding.tvUploadLogo.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.toolbar.iv_back.setOnClickListener {
            activity?.finish()
        }
    }

    fun goHome() {
        showActivity(MainActivity::class.java, clearAllStack = true)

    }


    private fun openMaps() {
        MapBottomSheet.newInstance(object : onLocationClick {
            override fun onClick(lat: Double?, long: Double?, address: AddAddressParams?) {
                this@RegisterFragment.lat = lat.toString()
                this@RegisterFragment.lon = long.toString()
                this@RegisterFragment.address = address?.fullAddress
                if (address?.fullAddress?.isNull()==false) {
                    binding.etAddress.text = address?.fullAddress
                    // binding.btnLocateOnmap.setText(address?.fullAddress.toString())
                }
            }
        }, null).show(childFragmentManager, MapBottomSheet::class.java.canonicalName)
    }

    private fun checkLocation() {
        if (permissionManager.hasAllLocationPermissions()) {
            checkIfLocationEnabled()
        } else {
            permissionsLauncher?.launch(permissionManager.getAllLocationPermissions())
        }
    }

    private fun checkIfLocationEnabled() {
        if (locationManager.isLocationEnabled()) {
            openMaps()
        } else {
            activity?.let {
                locationManager.showAlertDialogButtonClicked(
                    it, locationSettingLauncher
                )
            }
        }
    }


    private val permissionsLauncher = requestAppPermissions { allIsGranted, _ ->
        if (allIsGranted) {
            checkIfLocationEnabled()
        } else {
            Toast.makeText(
                activity, getString(R.string.not_all_permissions_accepted), Toast.LENGTH_LONG
            ).show()
        }
    }

    private val locationSettingLauncher = openLocationSettingsResultLauncher {
        checkIfLocationEnabled()
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                CropImage.activity(uri).start(requireContext(), this)
            } else {
            }
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                result.uri?.let {
                    FileManager.from(requireActivity(), it)?.let { file ->
                      
                      
                            binding.ivLogo.loadImage(file , isCircular = true)
                            logo = file
                        
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //  val error = result.
                showToast(data?.extras.toString())
            }
        }
    }

}
