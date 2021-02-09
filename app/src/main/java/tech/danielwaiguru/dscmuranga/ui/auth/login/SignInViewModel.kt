package tech.danielwaiguru.dscmuranga.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.danielwaiguru.dscmuranga.models.ResultWrapper
import tech.danielwaiguru.dscmuranga.repositories.AuthRepository

class SignInViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _registerUiState = MutableStateFlow<ResultWrapper<FirebaseUser?>>(ResultWrapper.Empty)
    val registerUiSate: StateFlow<ResultWrapper<FirebaseUser?>> get() = _registerUiState
    private val _uiMessage = Channel<String>()
    val uiMessage get() = _uiMessage.receiveAsFlow()
    fun signIn(email: String, password: String) = viewModelScope.launch {
        _registerUiState.value = ResultWrapper.Loading
        try {
            val authResult = authRepository.signIn(email, password)
            _registerUiState.value = ResultWrapper.Success(authResult.user)
        } catch (e: Exception) {
            _registerUiState.value = ResultWrapper.Failure(e.message.toString())
        }
    }
}