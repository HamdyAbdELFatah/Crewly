package com.madar.crewly.di

import com.madar.crewly.core.common.DispatcherProvider
import com.madar.crewly.core.common.Mapper
import com.madar.crewly.core.data.AppDatabase
import com.madar.crewly.core.data.User
import com.madar.crewly.core.data.UserEntity
import com.madar.crewly.core.data.UserRepositoryImpl
import com.madar.crewly.core.data.UserEntityToUserMapper
import com.madar.crewly.core.data.UserToUserEntityMapper
import com.madar.crewly.core.data.createAppDatabase
import com.madar.crewly.core.domain.GetAllUsersUseCase
import com.madar.crewly.core.domain.SaveUserUseCase
import com.madar.crewly.core.domain.UserRepository
import com.madar.crewly.feature.display.DisplayViewModel
import com.madar.crewly.feature.input.InputViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}

val commonModule = module {
    single<DispatcherProvider> { AppDispatcherProvider() }
}

val dataModule = module {
    single { createAppDatabase(androidContext()) }
    single { get<AppDatabase>().userDao() }
    single<Mapper<UserEntity, User>>(named("entityToUser")) { UserEntityToUserMapper() }
    single<Mapper<User, UserEntity>>(named("userToEntity")) { UserToUserEntityMapper() }
    single<UserRepository> { 
        UserRepositoryImpl(
            get(), 
            get(named("entityToUser")), 
            get(named("userToEntity"))
        ) 
    }
}

val domainModule = module {
    factory { SaveUserUseCase(get()) }
    factory { GetAllUsersUseCase(get()) }
}

val inputModule = module {
    viewModel { InputViewModel(get(), get()) }
}

val displayModule = module {
    viewModel { DisplayViewModel(get(), get(), get()) }
}
