package com.dalhousie.habit.di

import com.dalhousie.habit.util.AuthorizationInterceptor
import com.dalhousie.habit.util.AuthorizationInterceptorImpl
import com.dalhousie.habit.util.components.AppSharedPreferences
import com.dalhousie.habit.util.components.AppSharedPreferencesImpl
import com.dalhousie.habit.util.components.NetworkHelper
import com.dalhousie.habit.util.components.NetworkHelperImpl
import com.dalhousie.habit.util.components.ResourceHelper
import com.dalhousie.habit.util.components.ResourceHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Defines all the classes that need to be provided in the scope of the app.
 * If they are singleton mark them as '@Singleton'.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingModule {

    @Singleton
    @Binds
    abstract fun bindResourceHelper(impl: ResourceHelperImpl): ResourceHelper

    @Singleton
    @Binds
    abstract fun bindNetworkHelper(impl: NetworkHelperImpl): NetworkHelper

    @Singleton
    @Binds
    abstract fun bindSharedPreferences(impl: AppSharedPreferencesImpl): AppSharedPreferences

    @Singleton
    @Binds
    abstract fun bindAuthorizationInterceptor(impl: AuthorizationInterceptorImpl): AuthorizationInterceptor
}