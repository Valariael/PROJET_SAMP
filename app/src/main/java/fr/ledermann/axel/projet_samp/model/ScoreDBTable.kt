package fr.ledermann.axel.projet_samp.model

import android.provider.BaseColumns

/*
 * This class is the database model for the score table.
 */
class ScoreDBTable : BaseColumns {
    companion object {
        const val NAME = "score"
        const val ID = "id"
        const val GOOD_ANSWERS = "good_answers"
        const val TOTAL_ANSWERS = "total_answers"
        const val ID_QUIZZ = "id_quizz"
        const val DATE = "date"
    }
}