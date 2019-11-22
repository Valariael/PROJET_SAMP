package fr.ledermann.axel.projet_samp.model

import java.io.Serializable

/*
 * This class is the model for a quiz.
 */
data class Quizz(var titleQuizz : String) : Serializable{
    var idQuizz : Long? = null

    constructor(titleQuizz: String, idQuizz: Long) : this(titleQuizz) {
        this.idQuizz = idQuizz
    }

    override fun toString(): String {
        return titleQuizz
    }
}