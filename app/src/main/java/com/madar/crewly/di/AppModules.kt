package com.madar.crewly.di

import com.madar.crewly.core.common.base.DispatcherProvider
import com.madar.crewly.core.common.mapper.Mapper
import com.madar.crewly.core.data.local.AppDatabase
import com.madar.crewly.core.data.local.entity.UserEntity
import com.madar.crewly.core.data.local.createAppDatabase
import com.madar.crewly.core.data.repository.UserRepositoryImpl
import com.madar.crewly.core.data.mapper.UserEntityToUserMapper
import com.madar.crewly.core.data.mapper.UserToUserEntityMapper
import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import com.madar.crewly.core.domain.usecase.GetAllUsersUseCase
import com.madar.crewly.core.domain.usecase.SaveUserUseCase
import com.madar.crewly.core.domain.usecase.GetUserByIdUseCase
import com.madar.crewly.core.domain.usecase.UpdateUserUseCase
import com.madar.crewly.core.domain.usecase.DeleteUserUseCase
import com.madar.crewly.core.domain.usecase.GetUserCountUseCase
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
    factory { GetUserByIdUseCase(get()) }
    factory { UpdateUserUseCase(get()) }
    factory { DeleteUserUseCase(get()) }
    factory { GetUserCountUseCase(get()) }
}

val inputModule = module {
    viewModel { InputViewModel(get(), get(), get(), get()) }
}

val displayModule = module {
    viewModel { DisplayViewModel(get(), get(), get(), get()) }
}
