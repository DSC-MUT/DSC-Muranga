package tech.danielwaiguru.dscmuranga.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.danielwaiguru.dscmuranga.models.ResultWrapper
import tech.danielwaiguru.dscmuranga.models.User
import tech.danielwaiguru.dscmuranga.repositories.AuthRepository

class SignUpViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _loginUiState = MutableStateFlow<ResultWrapper<FirebaseUser?>>(ResultWrapper.Empty)
    val loginUiSate: StateFlow<ResultWrapper<FirebaseUser?>> get() = _loginUiState
    private val _uiMessage = Channel<String>()
    val uiMessage get() = _uiMessage.receiveAsFlow()
    fun signUp(user: User, password: String) {
        viewModelScope.launch {
            _loginUiState.value = ResultWrapper.Loading
            try {
                val authResult = authRepository.signUp(user, password)
                _loginUiState.value = ResultWrapper.Success(authResult.user)
            } catch (e: Exception) {
                _loginUiState.value = ResultWrapper.Failure(e.message.toString())
                _uiMessage.send(e.message.toString())
            }
        }
    }
}