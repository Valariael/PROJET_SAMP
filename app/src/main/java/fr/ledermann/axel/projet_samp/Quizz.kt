package fr.ledermann.axel.projet_samp

import java.io.Serializable

class Quizz(var titleQuizz : String) : Serializable{
    var listQuestions : ArrayList<Question> = ArrayList()
    var idQuizz: Long? = null

    constructor(titleQuizz: String, listQuestions : ArrayList<Question>) : this(titleQuizz) {
        this.listQuestions = listQuestions
    }
}