package com.college.attendance.management

import `in`.stock.core.persistence.dataStore.AbstractDataStore
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDetailsDataStore @Inject constructor() : AbstractDataStore<UserDetails>(
    defaultValueProducer = { UserDetails.default() },
    kSerializer = UserDetails.serializer(),
    fileName = "user_details"
)

@Serializable
data class UserDetails(
    val uid: String = "",
    val userName: String,
    val role: UserRole,
    val enrolledIn: List<String>? = null
) {
    companion object {
        fun default() = UserDetails("", "", UserRole.None)
    }
}

enum class UserRole {
    None,
    Student,
    Teacher
}