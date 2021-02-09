package tech.danielwaiguru.dscmuranga.ui.auth.register

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
import tech.danielwaiguru.dscmuranga.databinding.FragmentSignUpBinding
import tech.danielwaiguru.dscmuranga.models.ResultWrapper
import tech.danielwaiguru.dscmuranga.models.User
import tech.danielwaiguru.dscmuranga.network.NetworkStatusChecker
import tech.danielwaiguru.dscmuranga.utils.common.CredentialValidator
import tech.danielwaiguru.dscmuranga.utils.extensions.snackBar

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val credentialValidator: CredentialValidator by inject()
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val networkStatusChecker: NetworkStatusChecker by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
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
    private fun setCredentials(
        fullName: String, email: String, password: String, cPassword: String) {
        credentialValidator.setCredentials(fullName, email, password, cPassword)
    }
    private fun showNameError() {
        if (!credentialValidator.isNameValid()) {
            binding.etName.error = getString(R.string.name_error)
        }
    }
    private fun showEmailError() {
        if (!credentialValidator.isEmailValid()){
            binding.emailLayout.error = getString(R.string.email_error)
        }
    }
    private fun showPasswordError() {
        if (!credentialValidator.isPasswordValid()){
            binding.passwordLayout.error = getString(R.string.password_error)
        }
    }
    private fun showNonMatchPasswordError() {
        if (!credentialValidator.isCPasswordValid()) {
            binding.cPasswordLayout.error = getString(R.string.password_match_error)
        }
    }
    private fun areInputsValid(): Boolean {
        showNameError()
        showEmailError()
        showPasswordError()
        showNonMatchPasswordError()
        return credentialValidator.areCredentialsValid()
    }
    private fun hasNoConnection() {
        requireView().snackBar(getString(R.string.network_error_message))
    }
    private fun userSignUp() {
        with(binding) {
            val fullName = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val cPassword = etCPassword.text.toString()
            val user = User(fullName, email)
            setCredentials(fullName, email, password, cPassword)
            if (areInputsValid()) {
                networkStatusChecker.performIfConnected(::hasNoConnection) {
                    signUpViewModel.signUp(user, password)
                }
            }
        }
    }
    private fun initListeners() {
        with(binding) {
            registerButton.setOnClickListener { userSignUp() }
        }
    }
    private fun getUiMessage(callback:(String)->Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            signUpViewModel.uiMessage.collect {
                callback(it)
            }
        }
    }
    private fun getUiState() {
        lifecycleScope.launchWhenStarted {
            signUpViewModel.loginUiSate.collect {
                when (it) {
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