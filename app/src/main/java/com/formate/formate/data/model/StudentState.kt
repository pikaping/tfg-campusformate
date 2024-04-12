package com.formate.formate.data.model

sealed class StudentState {
    object Loading: StudentState()
    data class Success(val message: String): StudentState()
    data class Error(val message: String): StudentState()
}
