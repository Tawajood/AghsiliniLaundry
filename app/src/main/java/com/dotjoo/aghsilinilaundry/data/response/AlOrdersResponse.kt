package com.dotjoo.aghsilinilaundry.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderInfoResponse(

    @SerializedName("order"             ) var order           : OrderInfo? = OrderInfo(),
    @SerializedName("total_items_price" ) var totalItemsPrice : String?   = null

) : Parcelable

@Parcelize
data class AlOrdersResponse(
    @SerializedName("orders") var orders: kotlin.collections.ArrayList<Order>  = arrayListOf(),

    ) : Parcelable


@Parcelize
data class Order(
    @SerializedName("id") var id: String? = null,
     @SerializedName("laundry") var laundry: String? = null,
     @SerializedName("items_count") var items_count: String? = null,
     @SerializedName("created_at") var created_at: String? = null,
     @SerializedName("status") var status: String? = null,
    @SerializedName("argent") var argent: Int? = null,

    @SerializedName("logo") var logo: String? = null,

    ) : Parcelable

@Parcelize
data class ServiceResponse(

    @SerializedName("service"         ) var services       : ArrayList<ServiceInLaundry> = arrayListOf(),

    ) : Parcelable

@Parcelize
data class ItemsInServiceResponse(

    @SerializedName("items"         ) var items       : ArrayList<ItemsInService> = arrayListOf(),

    ) : Parcelable

@Parcelize
data class ServiceInLaundry(

    @SerializedName("id" ) var itemId : String?    = null,
    @SerializedName("name"    ) var name   : String? = null,
    var choosen :Boolean? =  false

) : Parcelable

@Parcelize
data class ItemsInService(

    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("name"         ) var name        : Name?   = Name(),
    @SerializedName("price"        ) var price       : Int?    = null,
    @SerializedName("argent_price" ) var argentPrice : Int?    = null,
    @SerializedName("locale"       ) var locale      : String? = null,
    var count :Int ? =0

) : Parcelable

@Parcelize


data class Name (

    @SerializedName("ar" ) var ar : String? = null,
    @SerializedName("en" ) var en : String? = null

) : Parcelable

@Parcelize
data class OrderInfo(
    @SerializedName("id"            ) var id           : String?              = null,
    @SerializedName("argent"        ) var argent       : Int?              = null,
    @SerializedName("tax"           ) var tax          : String?              = null,
    @SerializedName("delivery"      ) var delivery     : String?              = null,
    @SerializedName("progress"      ) var progress     : String?              = null,
    @SerializedName("address"      ) var address     : String?              = null,
    @SerializedName("total"         ) var total        : String?              = null,
    @SerializedName("customer_phone" ) var customer_phone : String?           = null,
    @SerializedName("payment_type" ) var payment_type : Int?           = null,
    @SerializedName("customer_name" ) var customerName : String?           = null,
    @SerializedName("additional_cost") var additional_cost: String? = null,

    @SerializedName("lat"           ) var lat          : String?           = null,
    @SerializedName("lon"           ) var lon          : String?              = null,
    @SerializedName("laundry_id"    ) var laundryId    : String?              = null,
    @SerializedName("driver_id"     ) var driverId     : String?           = null,
    @SerializedName("orderitems"    ) var orderitems   : ArrayList<OrderInfoItem> = arrayListOf(),
    @SerializedName("laundry"       ) var laundry      : Laundry?          = Laundry(),
    @SerializedName("driver"        ) var driver       : Driver?           = null
) : Parcelable

@Parcelize
data class OrderInfoItem(


@SerializedName("id"         ) var id        : Int?    = null,
@SerializedName("order_id"   ) var orderId   : Int?    = null,
@SerializedName("item_id"    ) var itemId    : Int?    = null,
@SerializedName("count"      ) var count     : Int?    = null,
@SerializedName("price"      ) var price     : Int?    = null,
@SerializedName("created_at" ) var createdAt : String? = null,
@SerializedName("updated_at" ) var updatedAt : String? = null,
@SerializedName("item"       ) var item      : Item?   = Item()

) : Parcelable

@Parcelize
data class Item(
@SerializedName("id"           ) var id          : Int?    = null,
@SerializedName("laundry_id"   ) var laundryId   : Int?    = null,
@SerializedName("service_id"   ) var serviceId   : Int?    = null,
@SerializedName("price"        ) var price       : Int?    = null,
@SerializedName("argent_price" ) var argentPrice : Int?    = null,
@SerializedName("created_at"   ) var createdAt   : String? = null,
@SerializedName("updated_at"   ) var updatedAt   : String? = null,
@SerializedName("service"   ) var service   : ServiceInLaundry? = null,
    @SerializedName("name"         ) var name        : String? = null

) : Parcelable

@Parcelize
data class ReviewsResponse(

    @SerializedName("rate"   ) var rate   : String? = null,
    @SerializedName("reviews"   ) var reviews   : ArrayList<ReviewItem>? = arrayListOf(),

) : Parcelable

@Parcelize
data class ReviewItem(

    @SerializedName("rate"   ) var rate   : String? = null,
    @SerializedName("msg"   ) var msg   : String? = null,

) : Parcelable

@Parcelize
data class Driver(
    @SerializedName("id"           ) var id          : String?    = null,
    @SerializedName("country_code"   ) var country_code   : String?    = null,
    @SerializedName("phone"        ) var phone       : String?    = null,
    @SerializedName("driveing_licence" ) var driveing_licence : String?    = null,
    @SerializedName("car_form"   ) var car_form   : String? = null,
    @SerializedName("national_id"   ) var national_id   : String? = null,
    @SerializedName("name"         ) var name        : String? = null,
    @SerializedName("img"         ) var img        : String? = null,
    @SerializedName("updated_at"         ) var updated_at        : String? = null

) : Parcelable
