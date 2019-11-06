package fr.ledermann.axel.projet_samp

import java.io.Serializable

class Answer(var answer : String) :Serializable {
    var isOk : Boolean = false
    var idAnswer: Long? = null

    constructor(textAnswer: String, isOk : Boolean) : this(textAnswer) {
        this.isOk = isOk
    }
}