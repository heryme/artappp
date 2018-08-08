package com.artproficiencyapp.test.model

/**
 * Created by admin on 14-Jun-18.
 */

data class Question(
    val status_code: Int,
    val message: String,
    val data: List<Data>
)

data class Data(
    val id: Int,
    val name: String,
    val image_url: List<String>,
    val type: String,
    val qseconds: Int,
    val category_id: Int,
    val created_at: String,
    val updated_at: String,
    val extra: List<Extra>
)

data class Extra(
    val id: Int,
    val type: String,
    val value: String,
    val question_id: Int,
    val created_at: String,
    val updated_at: String
)