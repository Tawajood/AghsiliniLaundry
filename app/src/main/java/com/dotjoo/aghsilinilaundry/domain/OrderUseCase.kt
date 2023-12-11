package com.dotjoo.aghsilinilaundry.domain

 import com.dotjoo.aghsilinilaundry.base.BaseUseCase
import com.dotjoo.aghsilinilaundry.base.DevResponse
import com.dotjoo.aghsilinilaundry.base.ErrorResponse
import com.dotjoo.aghsilinilaundry.base.NetworkResponse
  import com.dotjoo.aghsilinilaundry.data.Repository
 import com.dotjoo.aghsilinilaundry.data.param.GetItemsInServiceParam
 import com.dotjoo.aghsilinilaundry.data.param.OrderInfoParam
 import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class OrderUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

companion object OrderTypes{
    val CURRENT =1
    val NEWw =2
    val PREV =3

}
        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params == CURRENT) {
                flow {
                    emit(repository.getCurrentOrder( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params == NEWw) {
                flow {
                    emit(repository.getNewOrder())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }  else  if (params == PREV) {
                flow {
                    emit(repository.getPrevOrder())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }    else  if (params is OrderInfoParam) {
                flow {
                    emit(repository.getOrderInfo(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(null)
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
}


