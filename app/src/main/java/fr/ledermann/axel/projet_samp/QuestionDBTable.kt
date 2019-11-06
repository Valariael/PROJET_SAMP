package fr.ledermann.axel.projet_samp

import android.provider.BaseColumns

class QuestionDBTable : BaseColumns {
    companion object {
        const val NAME = "question"
        const val ID = "id"
        const val TEXT = "text"
        const val ID_QUIZZ = "id_quizz"
    }
}