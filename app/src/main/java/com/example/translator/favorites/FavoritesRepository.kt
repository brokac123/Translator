package com.example.translator.favorites

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/*
class FavoritesRepository{
    private val database: FirebaseFirestore = Firebase.firestore

    fun saveTextToFirestore(text: String,context: Context) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            try {
                val userDocumentRef = database.collection("users").document(userId)
                userDocumentRef.update("FavoritedTranslations", FieldValue.arrayUnion(text))
                    .addOnSuccessListener {
                        Toast.makeText(context, "Translation added to favorites", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error favoriting translation", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {

                Toast.makeText(context, "Error favoriting translation", Toast.LENGTH_SHORT).show()
            }
        }
    }


    suspend fun getFavorites(): FavoriteTranslations? {

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        val data = userId?.let { database.collection("users").document(it).get().await() }

        return (data as DocumentSnapshot).toObject(FavoriteTranslations::class.java)
    }

    fun deleteFavoriteFromFirestore(text: String, context: Context) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            try {
                val userDocRef = database.collection("users").document(userId)
                userDocRef.update("FavoritedTranslations", FieldValue.arrayRemove(text))
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Translation deleted from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Error deleting from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } catch (e: Exception) {
                // Handle error
                Toast.makeText(
                    context,
                    "Error deleting translation from favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

 */

class FavoritesRepository {
    private val database: FirebaseFirestore = Firebase.firestore

    fun saveTextToFirestore(text: String, context: Context) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            createUserDocumentIfNotExists(userId) // Ensure the user document exists
            val userDocumentRef = database.collection("users").document(userId)
            userDocumentRef.update("FavoritedTranslations", FieldValue.arrayUnion(text))
                .addOnSuccessListener {
                    Toast.makeText(context, "Translation favorited", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error favoriting translation", Toast.LENGTH_SHORT).show()
                }
        }
    }

    suspend fun getFavorites(): FavoriteTranslations? {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        val data = userId?.let { database.collection("users").document(it).get().await() }

        return (data as DocumentSnapshot).toObject(FavoriteTranslations::class.java)
    }

    fun deleteFavoriteFromFirestore(text: String, context: Context) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            createUserDocumentIfNotExists(userId) // Ensure the user document exists
            val userDocRef = database.collection("users").document(userId)
            userDocRef.update("FavoritedTranslations", FieldValue.arrayRemove(text))
                .addOnSuccessListener {
                    Toast.makeText(context, "Translation deleted from favorites", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error deleting from favorites", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun createUserDocumentIfNotExists(userId: String) {
        val userDocumentRef = database.collection("users").document(userId)
        userDocumentRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val userData = mapOf("FavoritedTranslations" to emptyList<String>())
                userDocumentRef.set(userData)
            }
        }
    }
}
