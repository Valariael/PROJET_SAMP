package fr.ledermann.axel.projet_samp.model

import java.io.Serializable

/*
 * This class is the model for a question.
 */
data class Question(var textQuestion : String) : Serializable {
    var idQuizz : Long? = null
    var idQuestion : Long? = null

    constructor(textQuestion: String, idQuizz: Long, idQuestion: Long) : this(textQuestion) {
        this.idQuizz = idQuizz
        this.idQuestion = idQuestion
    }

    constructor(questionName: String, idQuizz: Long) : this(questionName) {
        this.idQuizz = idQuizz
    }
}