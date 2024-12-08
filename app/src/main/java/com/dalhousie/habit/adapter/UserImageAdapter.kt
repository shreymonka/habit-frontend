package com.dalhousie.habit.adapter

import androidx.databinding.ViewDataBinding
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.ItemUserImageBinding
import com.dalhousie.habit.model.UserImage
import com.dalhousie.habit.ui.base.BaseRecyclerAdapter

class UserImageAdapter(
    private val onUserImageClicked: (UserImage) -> Unit
) : BaseRecyclerAdapter<UserImage>() {

    val selectedUserImage: UserImage?
        get() = arrayList.find { it.isSelected }

    override fun getLayoutIdForType(viewType: Int): Int = R.layout.item_user_image

    override fun areItemsSame(firstItem: UserImage, secondItem: UserImage): Boolean =
        firstItem == secondItem

    override fun setDataForListItem(binding: ViewDataBinding, data: UserImage, position: Int) {
        (binding as? ItemUserImageBinding)?.apply {
            root.setOnClickListener { onUserImageClicked(data) }
        }
    }
}