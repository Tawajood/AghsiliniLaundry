package com.dotjoo.aghsilinilaundry.data.param



data class SendMessageParam(val msg :String="" )
data class ChargeWalletParam(val balance :String="",val fort_id :String="" )
data class WithdrawParam(val amount: String,
                         val bank_name: String  ,
                         val acount_number: String   )
