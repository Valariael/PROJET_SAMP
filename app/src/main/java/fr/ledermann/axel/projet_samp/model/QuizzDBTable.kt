package fr.ledermann.axel.projet_samp.model

import android.provider.BaseColumns

/*
 * This class is the database model for the quiz table.
 */
class QuizzDBTable : BaseColumns {
    companion object {
        const val NAME = "quizz"
        const val ID = "id"
        const val TITLE = "title"
    }
}