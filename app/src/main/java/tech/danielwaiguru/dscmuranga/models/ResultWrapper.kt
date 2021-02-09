package tech.danielwaiguru.dscmuranga.models

sealed class ResultWrapper<out T>{
    object Loading: ResultWrapper<Nothing>()
    object Empty: ResultWrapper<Nothing>()
    data class Success<T>(val data: T): ResultWrapper<T>()
    data class Failure<T>(val errorMessage: String?): ResultWrapper<T>()
}
