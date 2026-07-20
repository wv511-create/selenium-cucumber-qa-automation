package com.nammayantrashare.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

object FirebaseHelper {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Authentication
    fun getCurrentUser() = auth.currentUser

    // Machines
    suspend fun getMachines(): List<Machine> {
        return try {
            val snapshot = db.collection("machines").get().await()
            snapshot.documents.mapNotNull { it.toObject<Machine>()?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Bookings
    suspend fun getBookings(phone: String): List<Booking> {
        return try {
            val snapshot = db.collection("bookings")
                .whereEqualTo("farmerPhone", phone)
                .get().await()
            snapshot.documents.mapNotNull { it.toObject<Booking>()?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addBooking(booking: Booking) {
        try {
            db.collection("bookings").add(booking).await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun updateBookingStatus(id: String, status: BookingStatus) {
        try {
            db.collection("bookings").document(id).update("status", status).await()
            android.util.Log.d("FirebaseHelper", "Status updated successfully")
        } catch (e: Exception) {
            android.util.Log.e("FirebaseHelper", "Error updating status: ${e.message}")
        }
    }
}
