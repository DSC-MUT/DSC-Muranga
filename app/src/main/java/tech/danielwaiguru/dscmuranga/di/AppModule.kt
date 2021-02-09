package tech.danielwaiguru.dscmuranga.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.core.module.Module
import org.koin.dsl.module
import tech.danielwaiguru.dscmuranga.repositories.AuthRepository
import tech.danielwaiguru.dscmuranga.repositories.AuthRepositoryImpl
import tech.danielwaiguru.dscmuranga.ui.auth.login.SignInViewModel

private val firebaseModule: Module = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    single { Firebase.storage }
}
private val authRepositoryModule: Module = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}
private val viewModelModules: Module = module {
    single { SignInViewModel(get()) }
}

val appModules = listOf(firebaseModule, authRepositoryModule, viewModelModules)