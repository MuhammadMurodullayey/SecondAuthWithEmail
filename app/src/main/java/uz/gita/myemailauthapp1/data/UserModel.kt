package uz.gita.myemailauthapp1.data

import java.io.Serializable

data class UserModel(
    var name :String? = "",
    var email : String? ="",
    var password : String?="",
    var deviseSerialNumber: String?="",
    var imageUri: String?=""
    ) : Serializable