package com.dotjoo.aghsilinilaundry.ui.fragment.auth.register

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
 import com.dotjoo.aghsilinilaundry.data.param.AddAddressParams
import com.dotjoo.aghsilinilaundry.data.param.RegisterParams
import com.dotjoo.aghsilinilaundry.databinding.FragmentRegisterBinding
import com.dotjoo.aghsilinilaundry.ui.activity.AuthActivity
import com.dotjoo.aghsilinilaundry.ui.dialog.CheckOtpSheetFragment
import com.dotjoo.aghsilinilaundry.ui.dialog.OnPhoneCheckedWithOtp
import com.dotjoo.aghsilinilaundry.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghsilinilaundry.ui.fragment.auth.login.AuthViewModel
import com.dotjoo.aghsilinilaundry.util.FileManager
import com.dotjoo.aghsilinilaundry.util.PermissionManager
import com.dotjoo.aghsilinilaundry.util.WWLocationManager
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.ext.isNull
import com.dotjoo.aghsilinilaundry.util.ext.loadImage
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

    var lat: String? = ""
    var lon: String? = ""
    var address: String? = ""
    var verified_countryCode = ""
    var verified_phone: String? = null

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
                findNavController().navigate(R.id.waitingActivationFragment ,null,
                    NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                )
            }

            is AuthAction.ShowRegisterVaildation -> {
                showProgress(false)
               mViewModel.register(action.data)/* if (verified_phone.isNullOrEmpty() || verified_phone == null) {
                    verified_phone?.let { mViewModel.checkPhone(verified_countryCode, it) }
                  //  showCheckOtp(action.data)

                } else {
                    if (verified_phone == action.data.phone && verified_countryCode == action.data.country_code) {
                        mViewModel.register(action.data)
                    }
                    else {
                        showCheckOtp(action.data)
                    }
                }*/


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

    public fun  showCheckOtp(data: RegisterParams)
    {
        CheckOtpSheetFragment.newInstance("+" + binding.countryCodePicker.selectedCountryCode.toString(),
            binding.etPhone.text.toString(),
            object : OnPhoneCheckedWithOtp {
                override fun onClick(
                    country_code: String, phone: String, verifed: Boolean
                ) {
                    verified_phone = phone
                    verified_countryCode = country_code
                    mViewModel.register(data)
                }
            }).show(
            childFragmentManager, "CheckOtpSheetFragment"
        )
    }

    private fun onClick() {
        parent = requireActivity() as AuthActivity
        binding.toolbar.tv_title.setText(resources.getString(R.string.new_user))
        binding.tvTermsandcondito.setPaintFlags(binding.tvTermsandcondito.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        address?.let {
            binding.etAddress.setText(it)
        }
        logo?.let {
            binding.ivLogo.loadImage(it, isCircular = true)
        }

        binding.btnSignup.setOnClickListener {
            mViewModel.isVaildRegisteration(
                binding.etName.text.toString(),
                "+${binding.countryCodePicker.selectedCountryCode}",
                binding.etPhone.text.toString(),
                lat, lon, address,
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
findNavController().navigateUp()        }
        binding.tvTermsandcondito.setOnClickListener {
            findNavController().navigate(R.id.termsFragment2 )
                //null,
           //     NavOptions.Builder().setPopUpTo(R.id.registerFragment, false).build())
        }
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
