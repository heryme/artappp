package com.artproficiencyapp.model

/**
 * Created by admin on 21-May-18.
 */


data class Login(
    val status_code: Int,
    val message: String,
    val data: Data
)

data class Data(
    val id: Int,
    val name: String,
    val email: String,
    val phone_number: String,
    val designation: String,
    val director: String,
    val level_id: String,
    val level: String,
    val membership_society_id: String,
    val membership_society: String,
    val address: String,
    val lattitude: String,
    val longitude: String,
    val profile_image: String,
    val profile_image_thumb: String,
    val user_type: String,
    val device_type: String,
    val device_token: String,
    val login_type: String,
    val is_email_verified: String,
    val is_otp_verified: String,
    val is_director_verified: String,
    val director_name: String,
    val director_code: String,
    val token: String
)

