package tech.danielwaiguru.dscmuranga.utils.common

import android.util.Patterns

class CredentialValidatorImpl: CredentialValidator {
    lateinit var fullName: String
    lateinit var email: String
    lateinit var password: String
    lateinit var cPassword: String
    override fun setCredentials(
        fullName: String,
        email: String,
        password: String,
        cPassword: String
    ) {
        this.fullName = fullName
        this.email = email
        this.password = password
        this.cPassword = cPassword
    }

    override fun setSignInCredentials(email: String, password: String) {
        this.email = email
        this.password = password
    }

    override fun isEmailValid(): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun isNameValid(): Boolean = fullName.length > 6

    override fun isPasswordValid(): Boolean = password.length > 6

    override fun isCPasswordValid(): Boolean = cPassword == password
    override fun areCredentialsValid(): Boolean {
        return isNameValid() && isEmailValid() && isPasswordValid() && isCPasswordValid()
    }

    override fun areSignInCredentialsValid(): Boolean {
        return isEmailValid() && isPasswordValid()
    }
}