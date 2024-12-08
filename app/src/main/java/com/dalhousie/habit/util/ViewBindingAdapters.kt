package com.dalhousie.habit.util

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.databinding.BindingAdapter

/**
 * Set view's visibility gone.
 * @param isGone true to make view gone
 */
@BindingAdapter("showUnlessGone")
fun showUnlessGone(view: View, isGone: Boolean) {
    view.isGone = isGone
}

/**
 * Set view's visibility invisible.
 * @param isInvisible true to make view invisible
 */
@BindingAdapter("showUnlessInvisible")
fun showUnlessInvisible(view: View, isInvisible: Boolean) {
    view.isInvisible = isInvisible
}

/**
 * Binding adapter to set selection state for the view
 */
@BindingAdapter("android:isSelected")
fun View.setSelection(isSelected: Boolean) {
    this.isSelected = isSelected
}