package uz.gita.myemailauthapp1.domain.impl

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.myemailauthapp1.data.UserModel
import uz.gita.myemailauthapp1.data.local.MySharedPref
import uz.gita.myemailauthapp1.domain.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val firebase: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val mySharedPref: MySharedPref
) : AppRepository {
    private val db = fireStore.collection("usersWithEmail")

    override fun isUserHave(number: String): Flow<Result<UserModel>> =  callbackFlow <Result<UserModel>> {
        db.whereEqualTo("phoneNumber", number).get()
            .addOnSuccessListener {
                if(it.size() > 0) {
                    trySend(Result.success(it.first().toObject(UserModel::class.java)))
                }else{
                    trySend(Result.success(UserModel()))
                }
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }
        awaitClose {  }
    }.flowOn(Dispatchers.IO)

    override fun setImageUri(uri: Uri)= callbackFlow<Result<Unit>> {
        db.document(mySharedPref.email).update("imageUri",uri)
        Log.d("ttt","setIMAGE")
        awaitClose {  }
    }

    override fun logOut() = callbackFlow<Result<Unit>> {
        Log.d("ttt","logout email ${mySharedPref.email}")
        db.document(mySharedPref.email).delete().addOnSuccessListener {
            trySend(Result.success(Unit))
        }
        awaitClose {  }
    }

    override fun getUsers() = callbackFlow<Result<List<UserModel>>> {
        db.get()
            .addOnSuccessListener {
                val list = it.toObjects(UserModel::class.java)

                trySend(Result.success(list))
            }
            .addOnFailureListener {
                trySend(Result.failure(Throwable("Fail get user")))
            }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun addUser(user: UserModel) = callbackFlow<Result<Unit>> {
        db.document(user.email!!)
            .set(
                hashMapOf(
                    "name" to user.name,
                    "email" to user.email,
                    "password" to user.password,
                    "deviseSerialNumber" to user.deviseSerialNumber
                )
            )
            .addOnSuccessListener {
                trySend(Result.success(Unit))
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }

        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun registerUser(email: String, password: String) = callbackFlow<Result<Boolean>> {
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun loginUser(email: String, password: String) = callbackFlow<Result<Boolean>> {

        awaitClose { }
    }.flowOn(Dispatchers.IO)


}