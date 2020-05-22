package com.blogspot.android_czy_java.apps.mgr.main.profile.avatar

import com.blogspot.android_czy_java.apps.mgr.main.profile.avatar.usecase.ChangeAvatar
import javax.inject.Inject

class AvatarsPresenter @Inject constructor(private val changeAvatar: ChangeAvatar) {
    fun changeAvatar(id: String) {
        changeAvatar.execute(id)
    }
}