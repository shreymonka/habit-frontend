package com.dalhousie.habit.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("android:src")
fun ImageView.setImageDrawable(@DrawableRes id: Int) {
    setBackgroundResource(id)
}