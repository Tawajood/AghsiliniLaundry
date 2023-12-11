package com.dotjoo.aghsilinilaundry.domain

 import com.dotjoo.aghsilinilaundry.base.BaseUseCase
import com.dotjoo.aghsilinilaundry.base.DevResponse
import com.dotjoo.aghsilinilaundry.base.ErrorResponse
import com.dotjoo.aghsilinilaundry.base.NetworkResponse
  import com.dotjoo.aghsilinilaundry.data.Repository
 import com.dotjoo.aghsilinilaundry.data.param.ChangPasswordParam
 import com.dotjoo.aghsilinilaundry.data.param.UpdateProfileParam
 import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class ProfileUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
             return if (params is UpdateProfileParam) {

                    flow {
                        emit(repository.updateProfile(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

                }else if (params is ChangPasswordParam) {

                    flow {
                        emit(repository.changePassword(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

                }else if (params  == 1 ) {

                    flow {
                        emit(repository.deleteAccount( ))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

                }
              else {
                flow {
                    emit(repository.getProfile())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

         }
}




