package com.dotjoo.aghsilinilaundry.ui.fragment.settingFragments

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseViewModel
 import com.dotjoo.aghsilinilaundry.data.param.SendMessageParam
import com.dotjoo.aghsilinilaundry.data.param.WithdrawParam
import com.dotjoo.aghsilinilaundry.data.response.AboutusResponse
import com.dotjoo.aghsilinilaundry.data.response.AllMessagesResponse
import com.dotjoo.aghsilinilaundry.data.response.ContactResponse
import com.dotjoo.aghsilinilaundry.data.response.FinanceResponse
import com.dotjoo.aghsilinilaundry.data.response.NotifactionResponse
import com.dotjoo.aghsilinilaundry.data.response.TermsResponse
import com.dotjoo.aghsilinilaundry.domain.SettingUseCase
import com.dotjoo.aghsilinilaundry.domain.SettingUseCase.OrderTypes.About
import com.dotjoo.aghsilinilaundry.domain.SettingUseCase.OrderTypes.Contact
import com.dotjoo.aghsilinilaundry.domain.SettingUseCase.OrderTypes.Terms_
import com.dotjoo.aghsilinilaundry.domain.SettingWalletUseCase
 import com.dotjoo.aghsilinilaundry.util.NetworkConnectivity
import com.dotjoo.aghsilinilaundry.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingViewModel
@Inject constructor(
    app: Application,
    val useCase: SettingUseCase,
    val useCaseWallet: SettingWalletUseCase
) :
    BaseViewModel<SettingAction>(app) {

    fun getContact() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                useCase.invoke(
                    viewModelScope, Contact
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowContact(res.data?.data as ContactResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getTerms() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                useCase.invoke(
                    viewModelScope, Terms_
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowTerms(res.data?.data as TermsResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getAbout() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                useCase.invoke(
                    viewModelScope, About
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowAbout(res.data?.data as AboutusResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getMessages() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                useCase.invoke(
                    viewModelScope
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowAllMessage(res.data?.data as AllMessagesResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun sendMessages(msg: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                useCase.invoke(
                    viewModelScope, SendMessageParam(msg)
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowMessageSent(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun isVaildWithdraw(amount: String, bank_name: String, account_number: String) {
        if (amount.isNullOrBlank()) {
            produce(SettingAction.ShowFailureMsg(getString(R.string.msg_empty_amount)))
            false

        } else if (bank_name.isNullOrBlank()) {
            produce(SettingAction.ShowFailureMsg(getString(R.string.msg_empty_bank_name)))
            false

        } else if (account_number.isNullOrBlank()) {
            produce(SettingAction.ShowFailureMsg(getString(R.string.empty_account_number)))
            false

        } else withdrawBalance(
            amount, bank_name, account_number
        )
      

    }

    fun withdrawBalance(amount: String, bank_name: String, account_number: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                useCaseWallet.invoke(
                    viewModelScope, WithdrawParam(amount, bank_name, account_number)
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowBalnaceWithdraw(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getNotifaction() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                useCaseWallet.invoke(
                    viewModelScope, 1
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowNotifactions(res.data?.data as NotifactionResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getWallet() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                useCaseWallet.invoke(
                    viewModelScope
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowBalanceInWallet(res.data?.data as FinanceResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

}
