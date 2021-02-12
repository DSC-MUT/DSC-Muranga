package tech.danielwaiguru.dscmuranga.ui.auth.login

import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tech.danielwaiguru.dscmuranga.R
import tech.danielwaiguru.dscmuranga.databinding.FragmentSignInBinding
import tech.danielwaiguru.dscmuranga.models.ResultWrapper
import tech.danielwaiguru.dscmuranga.network.NetworkStatusChecker
import tech.danielwaiguru.dscmuranga.utils.Constants.RC_SIGN_IN
import tech.danielwaiguru.dscmuranga.utils.common.CredentialValidator
import tech.danielwaiguru.dscmuranga.utils.extensions.snackBar
import tech.danielwaiguru.dscmuranga.utils.extensions.toast

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val credentialValidator: CredentialValidator by inject()
    private val networkStatusChecker: NetworkStatusChecker by inject()
    private val signInViewModel: SignInViewModel by viewModel()
    private lateinit var progressDialog: SweetAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDialog()
        initListeners()
        getUiMessage {
            requireView().snackBar(it)
        }
        getUiState()
        setSpannable()
    }
    private fun initializeDialog() {
        progressDialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        progressDialog.titleText = getString(R.string.authenticating)
        progressDialog.progressHelper?.barColor = Color.parseColor("#863B96")
        progressDialog.setCancelable(false)
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
            binding.passwordLayout.error = getString(R.string.password_error)
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
    private fun navToSignUp() {
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }
    private fun setSpannable() {
        val spanText = SpannableString(getString(R.string.new_member))
        val clickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                navToSignUp()
            }
        }
        spanText.setSpan(clickable,
            17, spanText.length, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
        spanText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.linkColor)),
            17, spanText.length, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.navToSignUp.text = spanText
        binding.navToSignUp.movementMethod = LinkMovementMethod.getInstance()
    }
    private fun initListeners() {
        with(binding) {
            loginButton.setOnClickListener { userSignIn() }
            googleSocialAuth.setOnClickListener {
                showDialog()
                startGoogleSignInClient()
            }
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
                        showDialog()
                    }
                    is ResultWrapper.Failure -> {
                        hideDialog()
                        state.errorMessage?.let { requireView().snackBar(it) }
                    }
                    is ResultWrapper.Success -> {
                        hideDialog()
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                    }
                    is ResultWrapper.Empty -> {
                        hideDialog()
                    }
                }
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
    private fun getSignInClient(): GoogleSignInClient? {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireContext(), gso)
    }
    private fun startGoogleSignInClient() {
        val signInIntent = getSignInClient()?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN ) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { signInViewModel.firebaseAuthWithGoogle(it) }
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                requireContext().toast(getString(R.string.google_sign_in_success))
                hideDialog()
            } catch (e: Exception) {
                hideDialog()
                requireContext().toast(e.message.toString())
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}