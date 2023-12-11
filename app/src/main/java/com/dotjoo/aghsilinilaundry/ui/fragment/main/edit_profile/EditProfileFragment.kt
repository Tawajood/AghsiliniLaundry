package com.dotjoo.aghsilinilaundry.ui.fragment.main.edit_profile

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.param.AddAddressParams
import com.dotjoo.aghsilinilaundry.data.param.UpdateProfileParam
import com.dotjoo.aghsilinilaundry.data.response.Laundry
import com.dotjoo.aghsilinilaundry.databinding.FragmentEditProfileBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.dialog.ChangeDelteAccountSheetFragment
import com.dotjoo.aghsilinilaundry.ui.dialog.ChangePasswordSheetFragment
import com.dotjoo.aghsilinilaundry.ui.dialog.OnClickLoginFirst
import com.dotjoo.aghsilinilaundry.ui.fragment.auth.register.MapBottomSheet
import com.dotjoo.aghsilinilaundry.ui.fragment.auth.register.onLocationClick
import com.dotjoo.aghsilinilaundry.ui.fragment.main.account.AccountAction
import com.dotjoo.aghsilinilaundry.ui.fragment.main.account.AccountViewModel
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
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {
    lateinit var parent: MainActivity
    val mViewModel: AccountViewModel by activityViewModels()
    var countryCode = "+20"
    var lat: String? = ""
    var lon: String? = ""
    var address: String? = ""
    var logo : File? = null
    @Inject
    lateinit var locationManager: WWLocationManager

    @Inject
    lateinit var permissionManager: PermissionManager

    override fun onFragmentReady() {
        onclick()
         mViewModel.apply {
            getProfileData()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getProfileData()
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
    }



    fun handleViewState(action: AccountAction) {
        when (action) {
            is AccountAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AccountAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

      is AccountAction.ShowProfile ->  {
          action.data.laundry?.let { setupLaundry(it) }

            }
      is AccountAction.ProfileUpdated ->  {
          action.msg?.let { showToast(it) }

            }



            else -> {

            }
        }
    }


    private fun setupLaundry(laundry :Laundry) {
        laundry?.let {
            binding.etName.setText(it.name)
            binding.etAddress.setText(it.address)
          binding.ivProfile.loadImage(it.logo)
            binding.etPhone.setText(it.phone)
      binding.lytData.isVisible= true
            lat = laundry.lat
            lon = laundry.lon
            address = laundry.address
            countryCode = laundry.countryCode.toString()

        }
    }


    private fun onclick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.lytDeletAcc.setOnClickListener {
            ChangeDelteAccountSheetFragment.newInstance(object : OnClickLoginFirst {
                override fun onClick(choice: String) {}
            }).show(childFragmentManager, ChangeDelteAccountSheetFragment::class.java.canonicalName)

        }
        binding.lytChangePass.setOnClickListener {
         findNavController().navigate(R.id.editProfileFragment)
        }

        binding.cardClose.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSave.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }
        binding.btnEdititems.setOnClickListener {
            findNavController().navigate(R.id.editItemsFragment)
        }
       binding.etAddress.setOnClickListener {
           checkLocation()
       }
        binding.cardGallay.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        binding.btnSave.setOnClickListener {
          lat?.let { it1 ->
              mViewModel.updateProfile(  UpdateProfileParam(
                    binding.etName.text.toString(),
                    countryCode,
                    binding.etPhone.text.toString(),
                    it1, lon!!, address,logo))
            }


        }
    }

    private fun openMaps() {
        MapBottomSheet.newInstance(object : onLocationClick {
            override fun onClick(lat: Double?, long: Double?, address: AddAddressParams?) {
                this@EditProfileFragment.lat = lat.toString()
                this@EditProfileFragment.lon = long.toString()
                this@EditProfileFragment.address = address?.fullAddress
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

     private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            CropImage.activity(uri)
                .start(requireContext(), this)
        } else {
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                //  val resultUri: Uri = result.uri
                //      image = File(getRealPathFromURI(parent, resultUri))
                result.uri?.let {
                    FileManager.from(requireActivity(), it)?.let { file ->
                        logo = file

                        binding.ivProfile.loadImage(file, isCircular = true)
                     }
                }  }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //  val error = result.
            showToast(   data?.extras.toString())
        }
    }
}
