package tech.danielwaiguru.dscmuranga.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import tech.danielwaiguru.dscmuranga.models.User
import tech.danielwaiguru.dscmuranga.utils.Constants.USER_COLLECTION

class AuthRepositoryImpl(
    private val auth: FirebaseAuth, db: FirebaseFirestore): AuthRepository {
    private val userCollection = db.collection(USER_COLLECTION)
    override suspend fun signUp(user: User, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(user.email, password).await().apply {
            createUser(user)
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    override fun createUser(user: User) {
        userCollection.add(user)
            .addOnFailureListener {

            }
    }

    override suspend fun firebaseAuthWithGoogle(token: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(token, null)
        return auth.signInWithCredential(credential).await()
    }

    override fun signOut() = auth.signOut()
    override fun getCurrentUser(): FirebaseUser? = auth.currentUser
}