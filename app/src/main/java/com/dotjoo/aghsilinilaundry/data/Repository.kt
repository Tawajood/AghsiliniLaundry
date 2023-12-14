package com.dotjoo.aghsilinilaundry.data

import com.dotjoo.aghsilinilaundry.data.param.AddItemParam
import com.dotjoo.aghsilinilaundry.data.param.ChangPasswordParam
import com.dotjoo.aghsilinilaundry.data.param.CheckOtpWithPhoneParam
import com.dotjoo.aghsilinilaundry.data.param.CheckPhoneParam
import com.dotjoo.aghsilinilaundry.data.param.EditOrderBillParam
import com.dotjoo.aghsilinilaundry.data.param.GetItemsInServiceParam
 import com.dotjoo.aghsilinilaundry.data.param.LoginParams
import com.dotjoo.aghsilinilaundry.data.param.OrderInfoParam
import com.dotjoo.aghsilinilaundry.data.param.RegisterParams
import com.dotjoo.aghsilinilaundry.data.param.ResetPasswordParams
import com.dotjoo.aghsilinilaundry.data.param.SendMessageParam
import com.dotjoo.aghsilinilaundry.data.param.UpdateItemParam
import com.dotjoo.aghsilinilaundry.data.param.UpdateProfileParam
import com.dotjoo.aghsilinilaundry.data.param.WithdrawParam
import com.dotjoo.aghsilinilaundry.data.param.toMap
 import com.dotjoo.aghsilinilaundry.data.webService.ApiInterface
import com.dotjoo.aghsilinilaundry.fcm.FcmParam
import com.dotjoo.aghsilinilaundry.util.FileManager.toMultiPart
import javax.inject.Inject


class Repository @Inject constructor(private val api: ApiInterface) {
    suspend fun login(param: LoginParams) = api.login(param.countryCode, param.phone, param.pass)
    suspend fun updateFcmToken(param: FcmParam) = api.updateFcnToken(param.token)
     suspend fun register(param: RegisterParams) = api.register(
      param.toMap()
   ,param.logo.toMultiPart("logo")
    )

    suspend fun updateProfile(param: UpdateProfileParam) =
        api.updateProfile(param.toMap(), param.logo?.toMultiPart("logo"))

    suspend fun changePassword(param: ChangPasswordParam) =
        api.changePassword(param.pass, param.new_pass, param.new_pass_confirm)

    suspend fun logout() = api.logout()
    suspend fun deleteAccount() = api.deleteAccount()

    suspend fun resetpassword(param: ResetPasswordParams) =
        api.changePassAfterForgert(param.countryCode, param.phone, param.otp, param.pass)

    suspend fun checkPhone(param: CheckPhoneParam) = api.checkPhone(param.countryCode, param.phone)
    suspend fun checkOTpWIthPhone(param: CheckOtpWithPhoneParam) =
        api.checkOTpWIthPhone(param.countryCode, param.phone, param.otp)

    suspend fun getServices() = api.getServices()
    suspend fun getProfile() = api.getProfile()
    suspend fun getItemsInService(param: GetItemsInServiceParam) =
        api.getItemsInService(param.service_id)

    suspend fun getCurrentOrder() = api.getCurrentOrder()
    suspend fun getPrevOrder() = api.getPrevOrder()
    suspend fun getNewOrder() = api.getNewOrder()
    suspend fun getOrderInfo(param: OrderInfoParam) = api.getOrderInfo(param.orderID)
    suspend fun storeItem(param: AddItemParam) = api.storeItem(
        param.argent_price,
        param.price,
        param.laundry_id,
        param.service_id,
        param.ar_name,
        param.en_name,  )

    suspend fun updateItem(param: UpdateItemParam) = api.updateItem(
            param.argent_price,
        param.price,
        param. id,
         param.ar_name,
        param.en_name,  )


    suspend fun acceptOrder(param: OrderInfoParam) = api.acceptOrder(param.orderID)
    suspend fun editBill(param: EditOrderBillParam) = api.editBill(param.orderID , param.price , param.notes)
    suspend fun rejectOrder(param: OrderInfoParam) = api.rejectOrder(param.orderID)
    suspend fun reciveOrder(param: OrderInfoParam) = api.reciveOrder(param.orderID)
    suspend fun startprepareOrder(param: OrderInfoParam) = api.startprepareOrder(param.orderID)
    suspend fun endprepareOrder(param: OrderInfoParam) = api.endprepareOrder(param.orderID)
    suspend fun deliverOrder(param: OrderInfoParam) = api.deliverOrder(param.orderID)
    suspend fun getReviews() = api.getReviews()


    suspend fun getTerms_condition() = api.getTerms_condition()
    suspend fun getAbout() = api.getAbout()
    suspend fun getContact() = api.getContact()
    suspend fun getMessages() = api.getMessages()
    suspend fun getNotifaction() = api.getNotifaction()
    suspend fun getFinance() = api.getFinance()
    suspend fun withdraw(param: WithdrawParam) = api.withdraw(param.amount, param.bank_name , param.acount_number)
    suspend fun sendMessage(param: SendMessageParam) = api.sendMessage(param.msg)

}