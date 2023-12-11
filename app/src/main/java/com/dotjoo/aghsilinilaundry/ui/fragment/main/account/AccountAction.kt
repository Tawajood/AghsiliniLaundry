package com.dotjoo.aghsilinilaundry.ui.fragment.main.account

 import com.dotjoo.aghsilinilaundry.base.Action
 import com.dotjoo.aghsilinilaundry.data.response.ItemsInService
 import com.dotjoo.aghsilinilaundry.data.response.ItemsInServiceResponse
 import com.dotjoo.aghsilinilaundry.data.response.LoginResponse
 import com.dotjoo.aghsilinilaundry.data.response.ServiceResponse

sealed class AccountAction : Action {

    data class ShowLoading(val show: Boolean) : AccountAction()
    data class ShowFailureMsg(val message: String?) : AccountAction()


  data class  ShowProfile(val data : LoginResponse): AccountAction ()
  data class  ProfileUpdated(val msg : String): AccountAction ()
   data class  ShowItems(val data : ItemsInServiceResponse): AccountAction ()
   data class  ShowServices(val data : ServiceResponse): AccountAction ()
    data class ShowItemsStored(val msg : String ) : AccountAction()
    data class ShowItemUpdated(val msg : String ) : AccountAction()
    data class ChangePassword(val msg : String ) : AccountAction()
    data class AccountDeleted(val msg : String ) : AccountAction()
}
