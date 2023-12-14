package com.dotjoo.aghsilinilaundry.data.webService

import com.dotjoo.aghsilinilaundry.base.DevResponse
import com.dotjoo.aghsilinilaundry.base.ErrorResponse
import com.dotjoo.aghsilinilaundry.base.NetworkResponse
import com.dotjoo.aghsilinilaundry.data.response.AboutusResponse
import com.dotjoo.aghsilinilaundry.data.response.AlOrdersResponse
import com.dotjoo.aghsilinilaundry.data.response.AllMessagesResponse
import com.dotjoo.aghsilinilaundry.data.response.ContactResponse
import com.dotjoo.aghsilinilaundry.data.response.FinanceResponse
import com.dotjoo.aghsilinilaundry.data.response.ItemsInServiceResponse
import com.dotjoo.aghsilinilaundry.data.response.LoginResponse
import com.dotjoo.aghsilinilaundry.data.response.NotifactionResponse
import com.dotjoo.aghsilinilaundry.data.response.OrderInfoResponse
import com.dotjoo.aghsilinilaundry.data.response.ReviewsResponse
import com.dotjoo.aghsilinilaundry.data.response.ServiceResponse
import com.dotjoo.aghsilinilaundry.data.response.TermsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File
import javax.inject.Singleton

@Singleton
interface ApiInterface {

    @POST("laundry/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("laundry/update/fcmToken")
    @FormUrlEncoded
    suspend fun updateFcnToken(
        @Field("device_token") device_token: String
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("laundry/check/phone")
    @FormUrlEncoded
    suspend fun checkPhone(
        @Field("country_code") country_code: String, @Field("phone") phone: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("laundry/send/otp")
    @FormUrlEncoded
    suspend fun checkOTpWIthPhone(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("otp") otp: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("laundry/reset/password")
    @FormUrlEncoded
    suspend fun changePassAfterForgert(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("otp") otp: String,
        @Field("password") password: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>
 @POST("laundry/edit/order")
    @FormUrlEncoded
    suspend fun editBill(
        @Field("order_id") order_id: String,
        @Field("price") price: String,
        @Field("note") notes: String,
     ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("laundry/register")
    @Multipart
    @JvmSuppressWildcards
    suspend fun register(

        @PartMap updateMap: Map<String, RequestBody>, @Part image: MultipartBody.Part?
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("profile/change-password")
    @FormUrlEncoded
    suspend fun changePassword(
        @Field("old_password") old_password: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("laundry/delete/account")
    @FormUrlEncoded
    suspend fun deleteAccount(
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("laundry/update/profile")
    @Multipart
    @JvmSuppressWildcards
    suspend fun updateProfile(
        @PartMap updateMap: Map<String, RequestBody>, @Part image: MultipartBody.Part?
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @GET("general/get/services")
    suspend fun getServices(
    ): NetworkResponse<DevResponse<ServiceResponse>, ErrorResponse>

    @GET("laundry/get/Profile")
    suspend fun getProfile(
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @GET("laundry/get/items?service_id=1")
    suspend fun getItemsInService(
        @Query("service_id") service_id: String
    ): NetworkResponse<DevResponse<ItemsInServiceResponse>, ErrorResponse>

    @POST("laundry/logout")
    @FormUrlEncoded
    suspend fun logout(): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("laundry/current/order")
    suspend fun getCurrentOrder(
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("laundry/new/order")
    suspend fun getNewOrder(
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("laundry/previous/order")
    suspend fun getPrevOrder(
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("laundry/get/order")
    suspend fun getOrderInfo(
        @Query("order_id") order_id: String,

        ): NetworkResponse<DevResponse<OrderInfoResponse>, ErrorResponse>

    @POST("laundry/store/item")
    @FormUrlEncoded
    suspend fun storeItem(
        @Field("argent_price") argent_price: String,
        @Field("price") price: String,
        @Field("laundry_id") laundry_id: String,
        @Field("service_id") service_id: String,
        @Field("ar_name") ar_name: String,
        @Field("en_name") en_name: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>


    @FormUrlEncoded
    @POST("laundry/update/item")
    suspend fun updateItem(
        @Field("argent_price") argent_price: String,
        @Field("price") price: String,
        @Field("id") id: String,
        @Field("ar_name") ar_name: String,
        @Field("en_name") en_name: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>



    @GET("laundry/accept/order")
    suspend fun acceptOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("laundry/reject/order")
    suspend fun rejectOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>
    @GET("laundry/recive/order")
    suspend fun reciveOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("laundry/start/prepare/order")
    suspend fun startprepareOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>


    @GET("laundry/end/prepare/order")
    suspend fun endprepareOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("laundry/deliver/order")
    suspend fun deliverOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("laundry/get/reviews")
    suspend fun getReviews(
    ): NetworkResponse<DevResponse<ReviewsResponse>, ErrorResponse>
    @GET("general/terms&condation")
    suspend fun getTerms_condition(
    ): NetworkResponse<DevResponse<TermsResponse>, ErrorResponse>

    @GET("general/aboutus")
    suspend fun getAbout(
    ): NetworkResponse<DevResponse<AboutusResponse>, ErrorResponse>

    @GET("laundry/get/notifications")
    suspend fun getNotifaction(
    ): NetworkResponse<DevResponse<NotifactionResponse>, ErrorResponse>

    @GET("general/contact")
    suspend fun getContact(
    ): NetworkResponse<DevResponse<ContactResponse>, ErrorResponse>

    @GET("general/get/messages/?type=laundry")
    suspend fun getMessages(
    ): NetworkResponse<DevResponse<AllMessagesResponse>, ErrorResponse>
 @GET("laundry/get/finance")
    suspend fun getFinance(
    ): NetworkResponse<DevResponse<FinanceResponse>, ErrorResponse>

    @FormUrlEncoded
    @POST("general/send/message")
    suspend fun sendMessage(
        @Field("message") message: String,
        @Field("type") type: String = "laundry",
    ): NetworkResponse<DevResponse<AllMessagesResponse>, ErrorResponse>

   @FormUrlEncoded
   @POST("laundry/withdraw/request")
    suspend fun withdraw(
        @Field("amount")       amount: String,
        @Field("bank_name")         bank_name: String  ,
        @Field("acount_number")  acount_number: String  ,
    ): NetworkResponse<DevResponse<AllMessagesResponse>, ErrorResponse>


}