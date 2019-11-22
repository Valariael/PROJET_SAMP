package fr.ledermann.axel.projet_samp.model

import android.provider.BaseColumns

/*
 * This class is the database model for the question table.
 */
class QuestionDBTable : BaseColumns {
    companion object {
        const val NAME = "question"
        const val ID = "id"
        const val TEXT = "text"
        const val ID_QUIZZ = "id_quizz"
    }
}