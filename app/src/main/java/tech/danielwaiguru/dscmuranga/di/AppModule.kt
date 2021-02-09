package tech.danielwaiguru.dscmuranga.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import tech.danielwaiguru.dscmuranga.network.NetworkStatusChecker
import tech.danielwaiguru.dscmuranga.repositories.AuthRepository
import tech.danielwaiguru.dscmuranga.repositories.AuthRepositoryImpl
import tech.danielwaiguru.dscmuranga.ui.auth.login.SignInViewModel
import tech.danielwaiguru.dscmuranga.ui.auth.register.SignUpViewModel
import tech.danielwaiguru.dscmuranga.utils.common.CredentialValidator
import tech.danielwaiguru.dscmuranga.utils.common.CredentialValidatorImpl

private val firebaseModule: Module = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    single { Firebase.storage }
}
private val authRepositoryModule: Module = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}
private val viewModelModules: Module = module {
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}
private val validatorModule: Module = module {
    single <CredentialValidator>{ CredentialValidatorImpl() }
}
private val networkStatusChecker: Module = module {
    factory { NetworkStatusChecker(androidContext()) }
}
val appModules = listOf(
    firebaseModule,
    authRepositoryModule,
    viewModelModules,
    validatorModule,
    networkStatusChecker
)