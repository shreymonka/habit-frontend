package com.dalhousie.habit.util.extensions

import android.text.method.PasswordTransformationMethod
import android.widget.EditText

/**
 * Set the password transformation method for an edittext
 * @param isHidden True if password is to be hidden, otherwise false
 */
fun EditText.setPasswordHiddenStatus(isHidden: Boolean) {
    transformationMethod = if (isHidden) PasswordTransformationMethod() else null
    setSelection(text?.length ?: 0)
}