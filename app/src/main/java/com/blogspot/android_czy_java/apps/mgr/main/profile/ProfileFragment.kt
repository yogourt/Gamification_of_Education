package com.blogspot.android_czy_java.apps.mgr.main.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.chat.ChatFragment
import com.blogspot.android_czy_java.apps.mgr.main.profile.avatar.ChooseAvatarBottomSheetDialog
import com.bumptech.glide.Glide
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_profile.view.*
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var presenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        prepareLayout(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun prepareLayout(view: View) {
        presenter.pointsLiveData.observe(this, Observer { view.points.text = it.toString() })
        presenter.userLiveData.observe(this, Observer {
            view.name.text = it.nickname
            view.user_photo.let { photoIV ->
                val resource = getPhotoRes(it.photo, photoIV.context)
                Glide.with(this).load(resource).into(photoIV)
            }
        })

        view.avatar_edit.setOnClickListener {
            openAvatarChooserDialog()
        }
    }

    private fun getPhotoRes(photo: String?, context: Context) =
        resources.getIdentifier(photo ?: "ic_profile", "drawable", context.packageName)

    private fun openAvatarChooserDialog() {
        fragmentManager?.let { ChooseAvatarBottomSheetDialog.getInstance().show(it, null) }
    }
}