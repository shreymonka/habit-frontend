package com.dalhousie.habit.ui.home_bottom_navigation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.dalhousie.habit.databinding.LayoutHomeBottomNavigationBinding
import com.dalhousie.habit.enums.HomeBottomNavigationItem
import com.dalhousie.habit.enums.HomeBottomNavigationItem.DASHBOARD

class HomeBottomNavigation : ConstraintLayout {

    private lateinit var binding: LayoutHomeBottomNavigationBinding
    private val clickHandler by lazy { ClickHandler() }

    private var clickListener: HomeBottomNavigationItemClickListener? = null

    constructor(context: Context) : super(context) {
        initializeView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeView(context)
    }

    fun setClickListener(clickListener: HomeBottomNavigationItemClickListener) {
        this.clickListener = clickListener
    }

    fun setSelectedItem(item: HomeBottomNavigationItem) {
        binding.selectedItem = item
    }

    private fun initializeView(context: Context) {
        binding = LayoutHomeBottomNavigationBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        binding.clickHandler = clickHandler
        binding.btnDashboard.isSelected = true
        setSelectedItem(DASHBOARD)
    }

    inner class ClickHandler {
        fun onItemClicked(item: HomeBottomNavigationItem) {
            setSelectedItem(item)
            clickListener?.onItemClicked(item)
        }
    }
}
