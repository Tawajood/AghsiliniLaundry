package com.dotjoo.aghsilinilaundry.domain

import com.dotjoo.aghsilinilaundry.base.BaseUseCase
import com.dotjoo.aghsilinilaundry.base.DevResponse
import com.dotjoo.aghsilinilaundry.base.ErrorResponse
import com.dotjoo.aghsilinilaundry.base.NetworkResponse
  import com.dotjoo.aghsilinilaundry.data.Repository
import com.dotjoo.aghsilinilaundry.data.param.WithdrawParam
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class SettingWalletUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params is WithdrawParam) {
                flow {
                    emit(repository.withdraw(params ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
else if (params==1) {
                flow {
                    emit(repository.getNotifaction( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

            else {
                flow {
                    emit(repository.getFinance())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


