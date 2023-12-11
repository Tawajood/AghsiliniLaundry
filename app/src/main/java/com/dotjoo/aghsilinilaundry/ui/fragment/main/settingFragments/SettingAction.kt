package com.dotjoo.aghsilinilaundry.ui.fragment.settingFragments

import com.dotjoo.aghsilinilaundry.base.Action
import com.dotjoo.aghsilinilaundry.data.response.AboutusResponse
import com.dotjoo.aghsilinilaundry.data.response.AllMessagesResponse
import com.dotjoo.aghsilinilaundry.data.response.ContactResponse
import com.dotjoo.aghsilinilaundry.data.response.FinanceResponse
import com.dotjoo.aghsilinilaundry.data.response.NotifactionResponse
import com.dotjoo.aghsilinilaundry.data.response.TermsResponse

sealed class SettingAction : Action {

    data class ShowLoading(val show: Boolean) : SettingAction()
    data class ShowFailureMsg(val message: String?) : SettingAction()

    data class ShowMessageSent(val message: String?) : SettingAction()
    data class ShowContact(val data: ContactResponse?) : SettingAction()
    data class ShowAllMessage(val data: AllMessagesResponse?) : SettingAction()
    data class ShowTerms(val data: TermsResponse) : SettingAction()
    data class ShowAbout(val data: AboutusResponse) : SettingAction()
    data class ShowBalanceInWallet(val data: FinanceResponse) : SettingAction()
    data class ShowNotifactions(val data: NotifactionResponse) : SettingAction()
    data class ShowBalnaceWithdraw(val message: String?) : SettingAction()
}
