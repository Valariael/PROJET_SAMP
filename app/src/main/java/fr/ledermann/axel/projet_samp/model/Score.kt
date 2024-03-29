package fr.ledermann.axel.projet_samp.model

import java.io.Serializable

/*
 * This class is the model for a score.
 */
data class Score(var goodAnswers : Int, var totalAnswers : Int, var idQuizz: Long) : Serializable {
    var date : String = ""
    var idScore : Long? = null

    constructor(goodAnswers : Int, totalAnswers : Int, idQuizz: Long, idScore : Long, date : String) : this(goodAnswers, totalAnswers, idQuizz) {
        this.idScore = idScore
        this.date = date
    }
}