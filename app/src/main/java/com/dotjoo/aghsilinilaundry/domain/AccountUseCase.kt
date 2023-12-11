package com.dotjoo.aghsilinilaundry.domain

 import com.dotjoo.aghsilinilaundry.base.BaseUseCase
import com.dotjoo.aghsilinilaundry.base.DevResponse
import com.dotjoo.aghsilinilaundry.base.ErrorResponse
import com.dotjoo.aghsilinilaundry.base.NetworkResponse
  import com.dotjoo.aghsilinilaundry.data.Repository
 import com.dotjoo.aghsilinilaundry.data.param.AddItemParam
 import com.dotjoo.aghsilinilaundry.data.param.GetItemsInServiceParam
  import com.dotjoo.aghsilinilaundry.data.param.UpdateItemParam
 import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AccountUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
             return if (params is AddItemParam) {
                     flow {
                        emit(repository.storeItem(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

                } else if (params is UpdateItemParam)     {
                    flow {
                        emit(repository.updateItem(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

                }
             else if (params is GetItemsInServiceParam) {
                flow {
                    emit(repository.getItemsInService(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            } else {
                flow {
                    emit(repository.getServices())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

         }
}




