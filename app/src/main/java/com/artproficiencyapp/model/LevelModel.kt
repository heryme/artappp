package com.artproficiencyapp.model

/**
 * Created by admin on 25-May-18.
 */

data class LevelModel(
    val status_code: Int,
    val message: String,
    val data: Datas
)

data class Datas(
    val level: List<Level>,
    val membership_societies: List<MembershipSociety>
)

data class Level(
    val id: Int,
    val name: String
)

data class MembershipSociety(
    val id: Int,
    val name: String
)