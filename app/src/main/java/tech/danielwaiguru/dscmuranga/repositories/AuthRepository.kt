package tech.danielwaiguru.dscmuranga.repositories

import com.google.firebase.auth.AuthResult
import tech.danielwaiguru.dscmuranga.models.User

interface AuthRepository {
    suspend fun signUp(user: User, password: String): AuthResult
    suspend fun signIn(email: String, password: String): AuthResult
    fun createUser(user: User)
}