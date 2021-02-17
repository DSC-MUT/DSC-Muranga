package tech.danielwaiguru.dscmuranga.utils.common

interface CredentialValidator {
    fun setCredentials(fullName: String, email: String, password: String, cPassword: String)
    fun setSignInCredentials(email: String, password: String)
    fun isEmailValid(): Boolean
    fun isNameValid(): Boolean
    fun isPasswordValid(): Boolean
    fun isCPasswordValid(): Boolean
    fun areCredentialsValid(): Boolean
    fun areSignInCredentialsValid(): Boolean
}