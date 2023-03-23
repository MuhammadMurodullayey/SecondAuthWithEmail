package uz.gita.myemailauthapp1.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import uz.gita.myemailauthapp1.data.UserModel

interface AppRepository {

    fun registerUser(email : String,password : String) : Flow<Result<Boolean>>
    fun loginUser(email : String,password : String): Flow<Result<Boolean>>
    fun getUsers() : Flow<Result<List<UserModel>>>
    fun addUser(user : UserModel)  : Flow<Result<Unit>>
    fun isUserHave(number: String)  : Flow<Result<UserModel>>
    fun setImageUri(uri: Uri): Flow<Result<Unit>>
    fun logOut() : Flow<Result<Unit>>

}