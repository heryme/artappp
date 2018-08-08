package com.artproficiencyapp.model

/**
 * Created by admin on 28-May-18.
 */

data class ReportModel(
    val status_code: Int,
    val message: String,
    val data: List<Report>
)

data class Report(
    val id: Int,
    val name: String
)