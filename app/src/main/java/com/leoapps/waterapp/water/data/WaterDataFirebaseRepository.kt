package com.leoapps.waterapp.water.data

import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import com.leoapps.waterapp.water.domain.WaterDataRepository
import com.leoapps.waterapp.water.domain.model.Drink
import com.leoapps.waterapp.water.domain.model.WaterData
import com.leoapps.waterapp.water.domain.model.WaterRecord
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

data class WaterDataTest(
    val goal: Int = 0,
    val records: List<Drink> = emptyList()
)

class WaterDataFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository,
) : WaterDataRepository {
    override suspend fun getUserWaterDataAsFlow(userId: String): Flow<WaterData?> {
        if (!isUserWaterDataInitialized(userId)) {
            initializeUserWaterData(userId)
        }

        return callbackFlow {
            val listenerRegistration = firestore
                .collection("users")
                .document(userId)
                .addSnapshotListener { snapshot, error ->
                    val waterData = try {
                        if (error == null) {
                            snapshot?.toObject<WaterData>()
                        } else {
                            Timber.e(error, "Firestore Snapshot Listener Error")
                            null
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Firestore Snapshot Listener Error")
                        null
                    }

                    Timber.d("New Water Data observed: $waterData")
                    trySend(waterData)
                }
            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun deleteDataForUser(userId: String) {
        firestore.collection("users")
            .document(userId)
            .delete()
            .await()
    }

    override suspend fun updateWaterGoal(userId: String, goal: Int) {
        firestore.collection("users")
            .document(userId)
            .set(mapOf("goal" to goal), SetOptions.merge())
            .await()
    }

    override suspend fun addWaterRecording(userId: String, drink: Drink) {
        val waterRecord = WaterRecord(
            drink = drink
        )

        firestore.collection("users")
            .document(userId)
            .update("records", FieldValue.arrayUnion(waterRecord))
            .await()
    }

    private suspend fun initializeUserWaterData(userId: String): TaskResult<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .set(WaterData())
                .await()

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

            val user = document.toObject<User>() ?: throw NullPointerException("user is null")

            TaskResult.Success(user)
        } catch (e: FirebaseException) {
            TaskResult.Failure(e)
        }
    }

    private suspend fun isUserWaterDataInitialized(userId: String): Boolean {
        val userDocumentRef = firestore.collection("users")
            .document(userId)
            .get()
            .await()

        return userDocumentRef.exists()
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