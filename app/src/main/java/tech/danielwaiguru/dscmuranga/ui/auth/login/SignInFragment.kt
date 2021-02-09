package tech.danielwaiguru.dscmuranga.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import tech.danielwaiguru.dscmuranga.R
import tech.danielwaiguru.dscmuranga.databinding.FragmentSignInBinding
import tech.danielwaiguru.dscmuranga.models.ResultWrapper
import tech.danielwaiguru.dscmuranga.network.NetworkStatusChecker
import tech.danielwaiguru.dscmuranga.utils.common.CredentialValidator
import tech.danielwaiguru.dscmuranga.utils.extensions.snackBar

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val credentialValidator: CredentialValidator by inject()
    private val networkStatusChecker: NetworkStatusChecker by inject()
    private val signInViewModel: SignInViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getUiMessage {
            requireView().snackBar(it)
        }
        getUiState()
    }
    private fun setCredentials(email: String, password: String) {
        credentialValidator.setSignInCredentials(email, password)
    }
    private fun showEmailError() {
        if (!credentialValidator.isEmailValid()) {
            binding.emailLayout.error = getString(R.string.email_error)
        }
    }
    private fun showPasswordError() {
        if (!credentialValidator.isPasswordValid()) {
            binding.emailLayout.error = getString(R.string.password_error)
        }
    }
    private fun areInputsValid(): Boolean {
        showEmailError()
        showPasswordError()
        return credentialValidator.areSignInCredentialsValid()
    }
    private fun hasNoConnection() {
        requireView().snackBar(getString(R.string.network_error_message))
    }
    private fun userSignIn() {
        with(binding) {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            setCredentials(email, password)
            if (areInputsValid()) {
                networkStatusChecker.performIfConnected(::hasNoConnection) {
                    signInViewModel.signIn(email, password)
                }
            }
        }
    }
    private fun initListeners() {
        with(binding) {
            loginButton.setOnClickListener { userSignIn() }
        }
    }
    private fun getUiMessage(result:(String) -> Unit) {
        lifecycleScope.launchWhenStarted {
            signInViewModel.uiMessage.collect {
                result(it)
            }
        }
    }
    private fun getUiState() {
        lifecycleScope.launchWhenStarted {
            signInViewModel.registerUiSate.collect { state ->
                when(state) {
                    is ResultWrapper.Loading -> {

                    }
                    is ResultWrapper.Failure -> {

                    }
                    is ResultWrapper.Success -> {

                    }
                    is ResultWrapper.Empty -> {

                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}