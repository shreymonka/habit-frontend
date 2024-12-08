package com.dalhousie.habit.util.components

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Resource helper is specially for [androidx.lifecycle.ViewModel] when we need resource but
 * we don't want to inject [Context] directly.
 */
interface ResourceHelper {

    /**
     * Provides string for the requested id
     * @param id Resource id of the string resource
     * Returns [String] of string [id] from resource.
     */
    fun getString(@StringRes id: Int): String

    /**
     * Provides string for the requested id with extra arguments
     * @param id Resource id of the string resource
     * @param arguments Array of extra arguments to format the string
     * Returns [String] of string [id] from resource.
     */
    fun getString(@StringRes id: Int, vararg arguments: Any): String
}

/**
 * Implementation of [ResourceHelper].
 */
@Singleton
class ResourceHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceHelper {

    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }

    override fun getString(@StringRes id: Int, vararg arguments: Any): String {
        // In order to pass an array to a function who takes vararg as parameter,
        // we need to use spread operator (*) which treats array elements as separate arguments
        // Ref: https://www.dhiwise.com/post/kotlin-varargs-the-guide-to-flexible-function-arguments
        return context.getString(id, *arguments)
    }
}