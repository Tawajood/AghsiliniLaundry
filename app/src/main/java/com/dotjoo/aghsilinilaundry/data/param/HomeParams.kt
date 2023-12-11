package com.dotjoo.aghsilinilaundry.data.param

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class GetItemsInServiceParam(val service_id :String="" )
data class AddItemParam( val argent_price: String,
                         val  price: String,
                         val laundry_id: String,
                         val    service_id: String,
                         val  ar_name: String,
                         val  en_name: String, )

data class UpdateItemParam( val argent_price: String,
                         val  price: String,
                         val id: String,
                          val  ar_name: String,
                         val  en_name: String, )
data class OrderInfoParam(val orderID: String = "", val type :Int? )

data class UpdateProfileParam(

    val name: String,
    val country_code: String,
    val phone: String,
    val lat: String,
    val lon: String,
    val address: String?,
    val logo: File?
)



fun UpdateProfileParam.toMap(): Map<String, RequestBody>{

    val itemMap = hashMapOf<String, RequestBody>()

    itemMap["name"] = name.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

    itemMap["lat"] = lat.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["lon"] = lon.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["country_code"] = country_code.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["phone"] = phone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["address"] = address.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())


    return itemMap
}