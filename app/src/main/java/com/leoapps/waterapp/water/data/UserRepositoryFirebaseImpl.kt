package com.leoapps.waterapp.water.data

import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import com.leoapps.waterapp.water.domain.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryFirebaseImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository,
) : UserRepository {

    private val currentUser = MutableStateFlow<User?>(null)

    //todo think about cancelation?
    private val scope = CoroutineScope(Job())

    init {
        scope.launch {
            authRepository.getCurrentUserAsFlow()
                .collect { authUser ->
                    when {
                        authUser == null -> {
                            currentUser.value = null
                        }

                        !userExists(authUser) -> {
                            addUser(authUser)
                            currentUser.value = authUser
                        }

                        else -> {
                            //todo update User
                            currentUser.value = authUser
                        }
                    }
                }
        }
    }

    override fun getCurrentUserAsFlow(): Flow<User?> {
        return currentUser.asStateFlow()
    }

    private suspend fun addUser(user: User): TaskResult<Unit> {
        return try {
            val userDocumentRef = firestore.collection("users")
                .document(user.id)
                .get()
                .await()

            if (!userDocumentRef.exists()) {
                firestore.collection("users")
                    .document(user.id)
                    .set(user)
                    .await()
            }

            TaskResult.Success(Unit)
        } catch (e: FirebaseException) {
            TaskResult.Failure(e)
        }
    }

    private suspend fun updateUser(user: User): TaskResult<Unit> {
        return try {
            firestore.collection("users")
                .document(user.id)
                .set(user)
                .await()

            TaskResult.Success(Unit)
        } catch (e: FirebaseException) {
            TaskResult.Failure(e)
        }
    }

    private suspend fun getUserByEmail(email: String): TaskResult<User> {
        return try {
            val document = firestore.collection("users")
                .document(email)
                .get()
                .await()

            val user =
                document.toObject<User>() ?: throw NullPointerException("user is null")

            TaskResult.Success(user)
        } catch (e: FirebaseException) {
            TaskResult.Failure(e)
        }
    }

    private suspend fun userExists(user: User): Boolean {
        val userDocumentRef = firestore.collection("users")
            .document(user.id)
            .get()
            .await()

        return userDocumentRef.exists()
    }

    override suspend fun deleteUser() {

    }

    private fun serverTimeStamp() {
        // If you're using custom Kotlin objects in Android, add an @ServerTimestamp
        // annotation to a Date field for your custom object classes. This indicates
        // that the Date field should be treated as a server timestamp by the object mapper.
        val docRef = firestore.collection("objects").document("some-id")

        // Update the timestamp field with the value from the server
        val updates = hashMapOf<String, Any>(
            "timestamp" to FieldValue.serverTimestamp(),
        )

        docRef.update(updates).addOnCompleteListener { }
    }
}