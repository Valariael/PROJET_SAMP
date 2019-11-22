package fr.ledermann.axel.projet_samp.model

import java.io.Serializable

/*
 * This class is the model for an answer.
 */
data class Answer(var answer : String) : Serializable {
    var isOk : Boolean = false
    var idQuestion : Long? = null
    var idAnswer : Long? = null

    constructor(textAnswer: String, isOk : Boolean, idQuestion : Long, idAnswer : Long) : this(textAnswer) {
        this.isOk = isOk
        this.idQuestion = idQuestion
        this.idAnswer = idAnswer
    }

    constructor(answerName: String, b: Boolean, idQuestion: Long) : this(answerName) {
        this.isOk = b
        this.idQuestion = idQuestion
    }
}