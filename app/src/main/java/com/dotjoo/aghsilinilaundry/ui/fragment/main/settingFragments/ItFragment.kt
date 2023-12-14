package com.dotjoo.aghsilinilaundry.ui.fragment.settingFragments

import android.text.TextUtils
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.data.response.Message
import com.dotjoo.aghsilinilaundry.databinding.FragmentItBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.adapter.MessagesAdapter
import com.dotjoo.aghsilinilaundry.util.Constants.EN
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItFragment : BaseFragment<FragmentItBinding>() {
    private val adapter by lazy { MessagesAdapter() }
    private var messages = mutableListOf<Message>()

    val mViewModel: SettingViewModel by viewModels()
    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            getMessages()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {

            mViewModel.getMessages()

            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }

    }

    fun handleViewState(action: SettingAction) {
        when (action) {
            is SettingAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is SettingAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowAllMessage -> {
                action.data?.messages?.toMutableList()?.let {
                    messages = it
                    adapter.messages = messages
                    binding.messagesRv.smoothScrollToPosition(adapter.messages.size - 1)
                }
            }

            else -> {

            }
        }
    }

    lateinit var parent: MainActivity
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.messagesRv.adapter = adapter
         binding.cardClose.setOnClickListener {
            findNavController().navigateUp()
        }
        if(PrefsHelper.getLanguage()==EN)binding.sendImg.rotation=90f
        else binding.sendImg.rotation=270f
        binding.cardSend.setOnClickListener {
            if (!TextUtils.isEmpty(binding.messageEt.text)) {
                mViewModel.sendMessages(binding.messageEt.text.toString())
                adapter.addMessage(
                    Message(

                        binding.messageEt.text.toString(), "send"
                    )
                )


                binding.messageEt.setText("")
                binding.messagesRv.smoothScrollToPosition(adapter.messages.size - 1)
            }
        }
    }


}