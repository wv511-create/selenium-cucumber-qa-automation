package com.nammayantrashare.app

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

// Core Data Models for the Namma-Yantra Share application
data class UserSession(
    val name: String = "",
    val phone: String = "",
    val village: String = "",
    val role: Role = Role.NONE
)

enum class Role { NONE, FARMER, OWNER }

data class Machine(
    val id: String,
    val name: String,
    val ownerName: String,
    val ownerPhone: String,
    val hourlyRate: Double,
    val dailyRate: Double,
    val isAvailable: Boolean,
    val rating: Int,
    val distance: String,
    val type: String
)

data class Booking(
    val id: String,
    val farmerName: String,
    val farmerPhone: String,
    val ownerPhone: String,
    val machineName: String,
    val date: String,
    val duration: String,
    val cost: Double,
    val status: BookingStatus,
    val purpose: String = ""
)

enum class BookingStatus { PENDING, ACCEPTED, DECLINED }

// In-memory data repository (Mocked for Firebase skeleton)
object Repository {
    val machines = mutableStateListOf(
        Machine("1", "Mahindra 575 DI Tractor", "Ramesh Kumar", "+91 98765 43210", 500.0, 3500.0, true, 4, "2.3km", "Tractor"),
        Machine("2", "Sonalika Harvester", "Suresh Patil", "+91 91234 56789", 800.0, 5500.0, false, 5, "4.1km", "Harvester"),
        Machine("3", "VST Power Tiller", "Lakshmi Devi", "+91 87654 32109", 300.0, 2000.0, true, 3, "1.8km", "Power Tiller"),
        Machine("4", "Honda Sprayer", "Anil Gowda", "+91 76543 21098", 200.0, 1200.0, true, 4, "3.5km", "Sprayer"),
        Machine("5", "John Deere 5050D Tractor", "Venkat Reddy", "+91 65432 10987", 600.0, 4200.0, true, 5, "5.2km", "Tractor"),
        Machine("6", "Kubota Harvester", "Meena Bai", "+91 54321 09876", 750.0, 5000.0, false, 3, "6.0km", "Harvester")
    )

    val bookings = mutableStateListOf(
        Booking("b1", "Kiran Kumar", "+91 94321 11111", "+91 98765 43210", "Mahindra Tractor", "15 May", "4hrs", 2000.0, BookingStatus.PENDING, "Ploughing"),
        Booking("b2", "Priya Nair", "+91 83210 22222", "+91 87654 32109", "VST Power Tiller", "16 May", "1 day", 2000.0, BookingStatus.PENDING, "Field prep"),
        Booking("b3", "Mohan Das", "+91 72109 33333", "+91 76543 21098", "Honda Sprayer", "17 May", "3hrs", 600.0, BookingStatus.PENDING, "Pesticide spray")
    )

    var userSession = mutableStateOf(UserSession())

    fun addBooking(booking: Booking) {
        bookings.add(0, booking)
    }

    fun updateBookingStatus(id: String, status: BookingStatus) {
        val index = bookings.indexOfFirst { it.id == id }
        if (index != -1) {
            bookings[index] = bookings[index].copy(status = status)
        }
    }

    fun addMachine(machine: Machine) {
        machines.add(0, machine)
    }
}
