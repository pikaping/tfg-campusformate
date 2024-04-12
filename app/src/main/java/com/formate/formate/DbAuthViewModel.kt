package com.formate.formate

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.formate.formate.data.model.StudentState
import com.formate.formate.data.network.DbClient.client
import com.formate.formate.utils.PrefHelper
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.launch

class DbAuthViewModel: ViewModel() {
    private val _studentState = mutableStateOf<StudentState>(StudentState.Loading)
    val studentState: State<StudentState> = _studentState

    fun signUp(
        context: Context,
        studentEmail: String,
        studentPassword: String,
    ) {
        viewModelScope.launch {
            try {
                client.gotrue.signUpWith(Email) {
                    email = studentEmail
                    password = studentPassword
                }
                setToken(context)
                _studentState.value = StudentState.Success("Alumno registrado con éxito!")
            } catch (e: HttpRequestTimeoutException) {
                _studentState.value = StudentState.Error("Error de red: ${e.message}")
            } catch (e: HttpRequestException) {
                _studentState.value = StudentState.Error("Error de conexión: ${e.message}")
            } catch (e: RestException) {
                _studentState.value = StudentState.Error("Error de la aplicación: ${e.message}")
            } catch (e: Exception) {
                _studentState.value = StudentState.Error("Error genérico: ${e.message}")
            }
        }
    }

    private fun setToken(context: Context) {
        viewModelScope.launch {
            val pref = PrefHelper(context)
            val token = client.gotrue.currentAccessTokenOrNull()
            pref.setStringData("token", token)
        }

    }

    private fun getToken(context: Context): String? {
        val pref = PrefHelper(context)
        return pref.getStringData("token")
    }

    fun signIn(
        context: Context,
        studentEmail: String,
        studentPassword: String,
    ) {
        viewModelScope.launch {
            try {
                client.gotrue.loginWith(Email) {
                    email = studentEmail
                    password = studentPassword
                }
                setToken(context)
                _studentState.value = StudentState.Success("Inicio de sesión con éxito!")
            } catch (e: HttpRequestTimeoutException) {
                _studentState.value = StudentState.Error("Error de red: ${e.message}")
            } catch (e: HttpRequestException) {
                _studentState.value = StudentState.Error("Error de conexión: ${e.message}")
            } catch (e: RestException) {
                _studentState.value = StudentState.Error("Error de la aplicación: ${e.message}")
            } catch (e: Exception) {
                _studentState.value = StudentState.Error("Error genérico: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                client.gotrue.logout()
                _studentState.value = StudentState.Success("Sesión cerrada con éxito!")
            } catch (e: HttpRequestTimeoutException) {
                _studentState.value = StudentState.Error("Error de red: ${e.message}")
            } catch (e: HttpRequestException) {
                _studentState.value = StudentState.Error("Error de conexión: ${e.message}")
            } catch (e: RestException) {
                _studentState.value = StudentState.Error("Error de la aplicación: ${e.message}")
            } catch (e: Exception) {
                _studentState.value = StudentState.Error("Error genérico: ${e.message}")
            }
        }
    }

    fun isAuthenticated(
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _studentState.value = StudentState.Error("Sesión no iniciada")
                } else {
                   client.gotrue.retrieveUser(token)
                   client.gotrue.refreshCurrentSession()
                   setToken(context)
                   _studentState.value = StudentState.Success("Sesión ya iniciada")
                }
                _studentState.value = StudentState.Success("Sesión cerrada con éxito!")
            } catch (e: HttpRequestTimeoutException) {
                _studentState.value = StudentState.Error("Error de red: ${e.message}")
            } catch (e: HttpRequestException) {
                _studentState.value = StudentState.Error("Error de conexión: ${e.message}")
            } catch (e: RestException) {
                _studentState.value = StudentState.Error("Error de la aplicación: ${e.message}")
            } catch (e: Exception) {
                _studentState.value = StudentState.Error("Error genérico: ${e.message}")
            }
        }
    }
}