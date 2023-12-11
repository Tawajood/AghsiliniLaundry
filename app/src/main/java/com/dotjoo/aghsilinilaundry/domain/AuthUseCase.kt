package com.dotjoo.aghsilinilaundry.domain


import com.dotjoo.aghsilinilaundry.base.BaseUseCase
import com.dotjoo.aghsilinilaundry.base.DevResponse
import com.dotjoo.aghsilinilaundry.base.ErrorResponse
import com.dotjoo.aghsilinilaundry.base.NetworkResponse
import com.dotjoo.aghsilinilaundry.data.Repository
import com.dotjoo.aghsilinilaundry.data.param.CheckOtpWithPhoneParam
import com.dotjoo.aghsilinilaundry.data.param.CheckPhoneParam
import com.dotjoo.aghsilinilaundry.data.param.LoginParams
import com.dotjoo.aghsilinilaundry.data.param.RegisterParams
import com.dotjoo.aghsilinilaundry.data.param.ResetPasswordParams
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AuthUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {


        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params is LoginParams) {
                flow {
                    emit(repository.login(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
           else  if (params is RegisterParams) {
                flow {
                  emit(repository.register(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }    else  if (params is ResetPasswordParams) {
                flow {
                  emit(repository.resetpassword(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is CheckPhoneParam) {

                    flow {
                        emit(repository.checkPhone(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

            }
            else  if (params is CheckOtpWithPhoneParam) {
                flow {
                    emit(repository.checkOTpWIthPhone(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(repository.logout())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


