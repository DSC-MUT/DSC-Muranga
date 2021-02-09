package tech.danielwaiguru.dscmuranga.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import tech.danielwaiguru.dscmuranga.databinding.FragmentSignInBinding
import tech.danielwaiguru.dscmuranga.utils.common.CredentialValidator

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val credentialValidator: CredentialValidator by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun setCredentials(
        fullName: String, email: String, password: String, cPassword: String) {
        credentialValidator.setCredentials(fullName, email, password, cPassword)
    }
    private fun showNameError() {
        if(!credentialValidator.isNameValid()) {
            //binding.et
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}