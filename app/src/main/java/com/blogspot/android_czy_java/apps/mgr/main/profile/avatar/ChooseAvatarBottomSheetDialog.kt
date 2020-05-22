package com.blogspot.android_czy_java.apps.mgr.main.profile.avatar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blogspot.android_czy_java.apps.mgr.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_choose_avatar.view.*
import javax.inject.Inject

class ChooseAvatarBottomSheetDialog : BottomSheetDialogFragment(),
    AvatarsAdapter.AvatarAdapterCallback {

    @Inject
    lateinit var presenter: AvatarsPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_avatar, container, false)
        prepareView(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun prepareView(view: View) {
        view.apply {
            this.avatars.apply {
                adapter = AvatarsAdapter(this@ChooseAvatarBottomSheetDialog)
            }
        }
    }

    override fun onAvatarClick(id: String) {
        presenter.changeAvatar(id)
        this.dialog?.hide()
    }

    companion object {
        fun getInstance() = ChooseAvatarBottomSheetDialog()
    }
}