package com.dalhousie.habit.di

import com.dalhousie.habit.data.repository.AuthenticationRepository
import com.dalhousie.habit.data.repository.AuthenticationRepositoryImpl
import com.dalhousie.habit.data.repository.HabitRepository
import com.dalhousie.habit.data.repository.HabitRepositoryImpl
import com.dalhousie.habit.data.repository.UserRepository
import com.dalhousie.habit.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Defines all the repositories that need to be provided in the scope of the app.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideAuthenticationRepository(
        authenticationRepository: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Singleton
    @Binds
    abstract fun provideHabitRepository(habitRepository: HabitRepositoryImpl): HabitRepository

    @Singleton
    @Binds
    abstract fun provideUserRepository(userRepository: UserRepositoryImpl): UserRepository
}