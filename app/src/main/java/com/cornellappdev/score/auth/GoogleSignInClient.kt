package com.cornellappdev.score.auth

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import com.cornellappdev.score.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class GoogleSignInClient @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun signIn(activity: Activity): GoogleIdentity {
        val option =
            GetSignInWithGoogleOption.Builder(activity.getString(R.string.default_web_client_id))
                .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val credential =
            CredentialManager.create(activity).getCredential(activity, request).credential
        require(
            credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        )
        val googleToken: GoogleIdTokenCredential =
            GoogleIdTokenCredential.createFrom(credential.data)
        val firebase = FirebaseAuth.getInstance()
        val result =
            firebase
                .signInWithCredential(GoogleAuthProvider.getCredential(googleToken.idToken, null))
                .await()
        val user = requireNotNull(result.user)
        val email = user.email.orEmpty()
        if (!email.endsWith("@cornell.edu", ignoreCase = true)) {
            firebase.signOut()
            throw CornellAccountRequiredException()
        }
        val idToken = requireNotNull(user.getIdToken(false).await().token)
        return GoogleIdentity(
            AuthUser(user.uid, user.displayName ?: email.substringBefore('@'), email),
            idToken,
        )
    }

    suspend fun signOut() {
        FirebaseAuth.getInstance().signOut()
        try {
            CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
        } catch (_: ClearCredentialException) {
            return
        }
    }
}

class CornellAccountRequiredException : Exception()
