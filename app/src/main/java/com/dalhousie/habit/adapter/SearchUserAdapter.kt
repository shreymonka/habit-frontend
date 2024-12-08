package com.dalhousie.habit.adapter

import androidx.databinding.ViewDataBinding
import com.dalhousie.habit.R
import com.dalhousie.habit.databinding.ItemUserBinding
import com.dalhousie.habit.model.User
import com.dalhousie.habit.ui.base.BaseRecyclerAdapter

class SearchUserAdapter(
    private val onUserClicked: (User) -> Unit
) : BaseRecyclerAdapter<User>() {
    override fun getLayoutIdForType(viewType: Int): Int = R.layout.item_user

    override fun areItemsSame(firstItem: User, secondItem: User): Boolean =
        firstItem.id == secondItem.id

    override fun setDataForListItem(binding: ViewDataBinding, data: User, position: Int) {
        (binding as? ItemUserBinding)?.apply {
            root.setOnClickListener { onUserClicked(data) }
        }
    }
}
