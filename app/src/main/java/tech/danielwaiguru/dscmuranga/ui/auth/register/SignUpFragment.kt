package tech.danielwaiguru.dscmuranga.ui.auth.register

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
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
    private val signUpViewModel: SignUpViewModel by viewModel()
    private val networkStatusChecker: NetworkStatusChecker by inject()
    private lateinit var progressDialog: SweetAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initializeDialog()
        getUiMessage {
            requireView().snackBar(it)
        }
        getUiState()
        setSpannable()
    }
    private fun initializeDialog() {
        progressDialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        progressDialog.titleText = getString(R.string.creating_account)
        progressDialog.progressHelper?.barColor = Color.parseColor("#863B96")
        progressDialog.setCancelable(false)
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
    private fun navToSignIn() {
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
    }
    private fun setSpannable() {
        val spanText = SpannableString(getString(R.string.registered))
        val clickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                navToSignIn()
            }
        }
        spanText.setSpan(clickable, 22, spanText.length, 0)
        spanText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.linkColor)),
        22, spanText.length, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
        binding.navToSignIn.text = spanText
        binding.navToSignIn.movementMethod = LinkMovementMethod.getInstance()
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
    private fun showDialog() {
        if (!progressDialog.isShowing){
            progressDialog.show()
        }
    }
    private fun hideDialog() {
        if (progressDialog.isShowing){
            progressDialog.dismiss()
        }
    }
    private fun getUiState() {
        lifecycleScope.launchWhenStarted {
            signUpViewModel.loginUiSate.collect { state->
                when (state) {
                    is ResultWrapper.Loading -> {
                        showDialog()
                    }
                    is ResultWrapper.Failure -> {
                        hideDialog()
                        state.errorMessage?.let { requireView().snackBar(it) }
                    }
                    is ResultWrapper.Success -> {
                        hideDialog()
                        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                    }
                    is ResultWrapper.Empty -> {
                        hideDialog()
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