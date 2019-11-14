package fr.ledermann.axel.projet_samp

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Quizz(var titleQuizz : String) : Serializable{
    var idQuizz : Long? = null

    constructor(titleQuizz: String, idQuizz: Long) : this(titleQuizz) {
        this.idQuizz = idQuizz
    }

    override fun toString(): String {
        return "$titleQuizz => $idQuizz"
    }
}