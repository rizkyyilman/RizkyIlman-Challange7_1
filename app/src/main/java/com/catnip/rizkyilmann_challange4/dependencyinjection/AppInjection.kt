package com.catnip.rizkyilmann_challange4.dependencyinjection

import DetailViewModel
import com.catnip.rizkyilmann_challange4.MainViewModel
import com.catnip.rizkyilmann_challange4.data.database.AppDatabase
import com.catnip.rizkyilmann_challange4.data.database.datasource.CartDataSource
import com.catnip.rizkyilmann_challange4.data.database.datasource.CartDatabaseDataSource
import com.catnip.rizkyilmann_challange4.data.repository.CartRepository
import com.catnip.rizkyilmann_challange4.data.repository.CartRepositoryImpl
import com.catnip.rizkyilmann_challange4.data.repository.ProductRepository
import com.catnip.rizkyilmann_challange4.data.repository.ProductRepositoryImpl
import com.catnip.rizkyilmann_challange4.data.repository.UserRepository
import com.catnip.rizkyilmann_challange4.data.repository.UserRepositoryImpl
import com.catnip.rizkyilmann_challange4.datastore.appDataStore
import com.catnip.rizkyilmann_challange4.network.api.datasource.AppApiDataSource
import com.catnip.rizkyilmann_challange4.network.api.datasource.AppDataSource
import com.catnip.rizkyilmann_challange4.network.api.service.AppApiService
import com.catnip.rizkyilmann_challange4.network.auth.FirebaseAuthDataSource
import com.catnip.rizkyilmann_challange4.network.auth.FirebaseAuthDataSourceImpl
import com.catnip.rizkyilmann_challange4.ui.cart.CartViewModel
import com.catnip.rizkyilmann_challange4.ui.checkout.CheckoutViewModel
import com.catnip.rizkyilmann_challange4.ui.home.HomeViewModel
import com.catnip.rizkyilmann_challange4.ui.profile.ProfileViewModel
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppInjection {

    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single { androidContext().appDataStore }
    }

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { AppApiService.invoke(get()) }
        single { FirebaseAuth.getInstance() }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
    }

    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<AppDataSource> { AppApiDataSource(get()) }
    }

    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
        single<ProductRepository> { ProductRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::CartViewModel)
        viewModelOf(::ProfileViewModel)
        viewModel { params -> DetailViewModel(params.get(), get()) }
        viewModel { params -> CheckoutViewModel(get()) }
        viewModelOf(::MainViewModel)
    }

    val modules: List<Module> = listOf(
        localModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule
    )
}
