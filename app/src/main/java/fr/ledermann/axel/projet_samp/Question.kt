package fr.ledermann.axel.projet_samp

import java.io.Serializable

class Question(var textQuestion : String) : Serializable{
    var listAnswers : ArrayList<Answer> = ArrayList()

    constructor(textQuestion: String, listAnswers : ArrayList<Answer>) : this(textQuestion) {
        this.listAnswers = listAnswers
    }
}