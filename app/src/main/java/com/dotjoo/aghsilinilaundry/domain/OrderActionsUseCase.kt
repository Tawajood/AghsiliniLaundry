package com.dotjoo.aghsilinilaundry.domain

import com.dotjoo.aghsilinilaundry.base.BaseUseCase
import com.dotjoo.aghsilinilaundry.base.DevResponse
import com.dotjoo.aghsilinilaundry.base.ErrorResponse
import com.dotjoo.aghsilinilaundry.base.NetworkResponse
import com.dotjoo.aghsilinilaundry.data.Repository
import com.dotjoo.aghsilinilaundry.data.param.EditOrderBillParam
import com.dotjoo.aghsilinilaundry.data.param.GetItemsInServiceParam
import com.dotjoo.aghsilinilaundry.data.param.OrderInfoParam
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class OrderActionsUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {

    companion object OrderTypes {
        val accept = 1
        val reject = 2
        val recive = 3
        val start_prep = 4
        val end_prep = 5
        val deliver_order = 6

    }

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return if (params is OrderInfoParam) {
            when (params.type) {
                accept -> {
                    flow {
                        emit(repository.acceptOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                reject -> {
                    flow {
                        emit(repository.rejectOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                recive -> {
                    flow {
                        emit(repository.reciveOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                start_prep -> {
                    flow {
                        emit(repository.startprepareOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                end_prep -> {
                    flow {
                        emit(repository.endprepareOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                deliver_order -> {
                    flow {
                        emit(repository.deliverOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                else -> {
                    flow {
                        emit(null)
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

                }
            }
        } else if (params is EditOrderBillParam) {
            flow {
                emit(repository.editBill(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else {
            flow {
                emit(null)
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        }

    }
}


