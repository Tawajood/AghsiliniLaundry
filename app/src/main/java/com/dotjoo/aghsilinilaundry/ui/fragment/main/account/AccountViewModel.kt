package com.dotjoo.aghsilinilaundry.ui.fragment.main.account

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseViewModel
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.data.param.AddItemParam
import com.dotjoo.aghsilinilaundry.data.param.ChangPasswordParam
import com.dotjoo.aghsilinilaundry.data.param.GetItemsInServiceParam
import com.dotjoo.aghsilinilaundry.data.param.UpdateItemParam
import com.dotjoo.aghsilinilaundry.data.param.UpdateProfileParam
import com.dotjoo.aghsilinilaundry.data.response.ItemsInService
import com.dotjoo.aghsilinilaundry.data.response.ItemsInServiceResponse
import com.dotjoo.aghsilinilaundry.data.response.LoginResponse
import com.dotjoo.aghsilinilaundry.data.response.ServiceInLaundry
import com.dotjoo.aghsilinilaundry.data.response.ServiceResponse
import com.dotjoo.aghsilinilaundry.domain.AccountUseCase
import com.dotjoo.aghsilinilaundry.domain.ProfileUseCase
 import com.dotjoo.aghsilinilaundry.util.NetworkConnectivity
import com.dotjoo.aghsilinilaundry.util.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountViewModel
@Inject constructor(
    app: Application,
    val useCase: AccountUseCase,
    val useCaseProfile: ProfileUseCase,
) : BaseViewModel<AccountAction>(app) {

    var urgent: Boolean = false
    var itemsInService: ItemsInService? = null
    var currentService: ServiceInLaundry? = null
    fun getServices() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.ShowServices(res.data?.data as ServiceResponse))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getProfileData() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                 useCaseProfile.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.ShowProfile(res.data?.data as LoginResponse))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun updateProfile(params: UpdateProfileParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseProfile.invoke(
                    viewModelScope, params
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                          res.data?.message?.let {   produce(AccountAction.ProfileUpdated(it)) }

                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getItemsInService(service_id: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, GetItemsInServiceParam(service_id)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.ShowItems(res.data?.data as ItemsInServiceResponse))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun isVaildStoreItem(
        argent_price: String?,
        price: String?,
        laundry_id: String,
        service_id: String,
        ar_name: String?,
        en_name: String?
    ):Boolean {
        if (argent_price.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_argent_price)))
            return false
        } else if (price.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_price)))
            return   false
        } else if (ar_name.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_ar_name)))
            return  false
        } else if (en_name.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_en_name)))
            return    false
        } else {
            storeItem(  argent_price, price, laundry_id, service_id, ar_name, en_name  )
          return  true
        }
    }
    fun isVaildUpdateItem(
        id:String,
        argent_price: String?,
        price: String?,
        ar_name: String?,
        en_name: String?
    ) :Boolean {
        if (argent_price.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_argent_price)))
            return    false
        } else if (price.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_price)))
            return   false
        } else if (ar_name.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_ar_name)))
            return   false
        } else if (en_name.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_en_name)))
            return   false
        } else {
            updateItem(  argent_price, price,   ar_name, en_name , id )
          return  true
        }
    }

    fun storeItem(
        argent_price: String,
        price: String,
        laundry_id: String,
        service_id: String,
        ar_name: String,
        en_name: String
    ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, AddItemParam(
                        argent_price, price, laundry_id, service_id, ar_name, en_name
                    )
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(
                                AccountAction.ShowItemsStored(
                                    res.data?.message as String
                                )
                            )
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun updateItem(
        argent_price: String, price: String, ar_name: String, en_name: String, id: String
    ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res =
                    useCase.invoke(viewModelScope,  id?.let {
                        UpdateItemParam(
                            argent_price, price, it, ar_name, en_name
                        )
                    }) { res ->
                        when (res) {
                            is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                            is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                            is Resource.Success -> {
                                produce(AccountAction.ShowItemUpdated(res.data?.message as String))
                            }
                        }
                    }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun isValidParamsChangePass(oldpass: String, newpass: String, confirmpass: String) {
        if (oldpass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_password)))
            false

        } else if (newpass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_new_password)))
            false

        } else if (confirmpass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
            false

        } else if (!confirmpass.equals(newpass)) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.both_passwords_must_match)))
            false

        } else changePassword(
            ChangPasswordParam( oldpass, newpass , confirmpass

        ))


    }

    fun changePassword(params: ChangPasswordParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseProfile.invoke(
                    viewModelScope, params
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(AccountAction.ChangePassword(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }  fun deleteAccount( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseProfile.invoke(
                    viewModelScope, 1
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(AccountAction.AccountDeleted(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }


}






