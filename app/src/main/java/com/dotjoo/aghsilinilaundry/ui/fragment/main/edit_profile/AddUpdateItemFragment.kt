package com.dotjoo.aghsilinilaundry.ui.fragment.main.edit_profile

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.data.response.ItemsInService
import com.dotjoo.aghsilinilaundry.databinding.FragmentAddUpdateItemBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.fragment.main.account.AccountAction
import com.dotjoo.aghsilinilaundry.ui.fragment.main.account.AccountViewModel
import com.dotjoo.aghsilinilaundry.ui.fragment.main.edit_profile.EditItemsFragment.Companion.Add
import com.dotjoo.aghsilinilaundry.ui.fragment.main.edit_profile.EditItemsFragment.Companion.AddAndUpdate
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.observe


class AddUpdateItemFragment : BaseFragment<FragmentAddUpdateItemBinding>() {
    val mViewModel: AccountViewModel by activityViewModels()
    lateinit var parent: MainActivity
    var type: Int? = null // additem or update
   var itemToEdit: ItemsInService? = null

    override fun onFragmentReady() {
      setupUi()

        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }

    }

    private fun setupUi() {
        type = arguments?.get(AddAndUpdate) as Int?
        if (type == Add) {
            binding.tvTitle.setText(resources.getString(R.string.add_item))
        } else {
           itemToEdit = arguments?.get("Item") as ItemsInService?
            setItemData()
            binding.tvTitle.setText(resources.getString(R.string.edit_item))
        }    }

    private fun setItemData() {
        itemToEdit?.let {
            binding.etNameAr.setText(it.name?.ar)
            binding.etNameEn.setText(it.name?.en)
            binding.etPrice.setText(it.price.toString())
            binding.etUrgentPrice.setText(it.argentPrice.toString())
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

            is AccountAction.ShowItemUpdated -> {
                showToast(action.msg)
                findNavController().navigateUp()
            }

            is AccountAction.ShowItemsStored -> {
                showToast(action.msg)
                findNavController().navigateUp()

            }


            else -> {

            }
        }
    }


    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)

        binding.cardBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSave.setOnClickListener {
            if (type == Add) {/*   argent_price: String?,
                   price: String?,
                   laundry_id: String,
                   service_id: String,
                   ar_name: String?,
                   en_name: String?*/
                PrefsHelper.getUserData()?.id?.let { it1 ->
                    mViewModel.currentService?.itemId?.let { it2 ->
                         binding.btnSave.isEnabled= !(    mViewModel.isVaildStoreItem(
                            binding.etUrgentPrice.text.toString(),
                            binding.etPrice.text.toString(),
                            it1,
                            it2,
                            binding.etNameAr.text.toString(),
                            binding.etNameEn.text.toString()
                        ))
                    }
                }
            } else {
                binding.btnSave.isEnabled = ! ( mViewModel.isVaildUpdateItem(
itemToEdit?.id.toString(),
                    binding.etUrgentPrice.text.toString(),
                    binding.etPrice.text.toString(),
                    binding.etNameAr.text.toString(),
                    binding.etNameEn.text.toString()
                ))
            }

        }
    }


}